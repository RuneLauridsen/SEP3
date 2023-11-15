using System.Net.Sockets;
using System.Text;
using GameClient;
using GameClient.Data;


namespace Shared.Data;

// TODO(rune): Måske gøre JWT til en del af protokollen, så alle request typerne
// ikke behøver at construct'es med en JWT?
public class ServiceSocket {
    private readonly string _url;
    private readonly int _port;
    private readonly Socket _socket;
    private NetworkStream _stream = null!;
    private BinaryWriter _writer = null!;
    private BinaryReader _reader = null!;

    // TODO(rune): HACK: Brug IAuthService i stedet, dvs. flyt IAuthService til Shared.
    public Func<string> _getJwtFunc = () => "";

    public ServiceSocket(string url, int port) {
        _url = url;
        _port = port;
        _socket = new Socket(SocketType.Stream, ProtocolType.Tcp);
    }

    public void Connect() {
        _socket.Connect(_url, _port);

        _stream = new NetworkStream(_socket);
        _writer = new BinaryWriter(_stream);
        _reader = new BinaryReader(_stream);
    }

    // NOTE(rune): Javas DataInputStream og DataOuputStream sender utf8-strings,
    // ved først at skrive længden i en big-endian 16-bit int.
    // SendString() svarer til DataInputStream.writeUTF() i Java.
    // ReadString() svarer til DataInputStream.readUTF() i Java.

    private void SendString(string s) {
        _writer.Write((byte)(s.Length >> 8));
        _writer.Write((byte)(s.Length >> 0));
        _writer.Write(Encoding.UTF8.GetBytes(s));
        _writer.Flush();
    }

    private string ReadString() {
        int length = 0;
        length |= _reader.ReadByte() << 8;
        length |= _reader.ReadByte() << 0;
        byte[] bytes = _reader.ReadBytes(length);
        string ret = Encoding.UTF8.GetString(bytes);
        return ret;
    }

    public void SendMessage(object body) {
        String bodyTypeName = body.GetType().Name;
        Head head = new Head(bodyTypeName, _getJwtFunc(), 0.0);
        string headString = JsonUtil.ToJson(head);
        string bodyString = JsonUtil.ToJson(body);
        string full = headString + "|" + bodyString;
        SendString(full);
    }

    public bool ReadMessage(out Head head, out object body) {
        string full = ReadString();
        int idx = full.IndexOf('|');
        if (idx > 0 && idx < full.Length - 1) {
            string headString = full.Substring(0, idx);
            string bodyString = full.Substring(idx + 1);

            head = JsonUtil.FromJson<Head>(headString);
            string bodyTypeName = typeof(Messages).FullName + "+" + head.bodyType; // NOTE(rune): "+" betyder nested class.
            Type? bodyType = Type.GetType(bodyTypeName);
            if (bodyType == null) {
                throw new Exception("Invalid response body type from game server (body type was '" + head.bodyType + "').");
            }

            body = JsonUtil.FromJson(bodyString, bodyType);
            return true;
        }
        else {
            throw new Exception("Invalid response from game server (could not find '|' marker).");
        }
    }

    public T SendAndReceive<T>(object requestBody) {
        long tickBegin = DateTime.Now.Ticks;
        SendMessage(requestBody);
        ReadMessage(out var responseHead, out var responseBody);
        long tickEnd = DateTime.Now.Ticks;
        long tickDiff = tickEnd - tickBegin;
        double elapsedClientMillis = (double)tickDiff / TimeSpan.TicksPerMillisecond;

        LogSendAndReceive(requestBody, responseBody, elapsedClientMillis, responseHead.elapsedServerMillis);

        if (responseBody is NotAuthorizedResponse) {
            throw new NotAuthorizedException();
        }

        // TODO(rune): if (reponse is not T) { throw ... }

        return (T)responseBody;
    }

    private static void LogSendAndReceive(object requestBody, object responseBody, double clientMillis, double serverMillis) {
        // TODO(rune): Bedre logging system. ILogger?
        Console.WriteLine($"[INFO {DateTime.Now:yyyy-MM-dd hh:mm:ss.fff}] {requestBody.GetType().Name} -> {responseBody.GetType().Name} (client elapsed {clientMillis} ms) (server elapsed {serverMillis} ms)");
    }
}
