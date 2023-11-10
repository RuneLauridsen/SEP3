using System.Net.Sockets;
using System.Text;


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

    private void SendString(string s) {
        _writer.Write((byte)(s.Length >> 8));
        _writer.Write((byte)(s.Length >> 0));
        _writer.Write(Encoding.UTF8.GetBytes(s));
        _writer.Flush();
    }

    public void SendMessage(object obj) {
        string head = obj.GetType().Name;
        string body = JsonUtil.ToJson(obj);
        string full = head + "|" + body;
        SendString(full);
    }

    private string ReadString() {
        int length = 0;
        length |= _reader.ReadByte() << 8;
        length |= _reader.ReadByte() << 0;
        byte[] bytes = _reader.ReadBytes(length);
        string ret = Encoding.UTF8.GetString(bytes);
        return ret;
    }

    public object ReadMessage() {
        string full = ReadString();
        int idx = full.IndexOf('|');
        if (idx > 0 && idx < full.Length - 1) {
            string head = full.Substring(0, idx);
            string body = full.Substring(idx + 1);

            string messageTypeName = typeof(Messages).FullName + "+" + head; // NOTE(rune): "+" betyder nested class.
            Type? messageType = Type.GetType(messageTypeName);
            if (messageType == null) {
                throw new Exception("Invalid response header from game server (header was '" + head + "').");
            }
            object ret = JsonUtil.FromJson(body, messageType);

            return ret;
        }
        else {
            throw new Exception("Invalid response from game server (could not find '|' marker).");
        }
    }

    public T SendAndReceive<T>(object request) {
        SendMessage(request);

        object response = ReadMessage();
        if (response is NotAuthorizedResponse) {
            throw new NotAuthorizedException();
        }

        // TODO(rune): if (reponse is not T) { throw ... }

        return (T)response;
    }
}
