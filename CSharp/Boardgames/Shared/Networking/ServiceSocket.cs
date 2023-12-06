using System.Net.Sockets;
using System.Runtime.CompilerServices;
using System.Text;
using GameClient;
using GameClient.Data;
using Shared.AuthService;
using Shared.AuthState;


namespace Shared.Data;

public class ServiceSocket {
    private readonly string _url;
    private readonly int _port;
    private readonly IAuthState _authState;
    private readonly bool _blocking;
    private Socket? _socket;
    private NetworkStream? _stream;

    public ServiceSocket(string url, int port, IAuthState authState) {
        _url = url;
        _port = port;
        _authState = authState;
    }

    public async Task Open() {
        _socket = new Socket(SocketType.Stream, ProtocolType.Tcp);
        await _socket.ConnectAsync(_url, _port);
        _stream = new NetworkStream(_socket);
    }

    public void Close() {
        _stream?.Close();
        _socket?.Close();

        _stream?.Dispose();
        _socket?.Dispose();
    }

    public async Task SendMessageAsync(object body, CancellationToken ct) {
        string jwt = await _authState.GetJwtAsync();
        Message m = new Message(body, jwt);
        String s = MessageSerializer.Serialize(m);
        await StringStreamer.SendStringAsync(_stream, s, ct);
    }

    public async Task<Message> ReadMessageAsync(CancellationToken ct) {
        String s = await StringStreamer.ReadStringAsync(_stream, ct);
        Message m = MessageSerializer.Deserialize(s);
        return m;
    }

    public Task<T?> SendAndReceiveAsync<T>(object requestBody) where T : class {
        return SendAndReceiveAsync<T>(requestBody, CancellationToken.None);
    }

    public async Task<T?> SendAndReceiveAsync<T>(object requestBody, CancellationToken ct) where T : class {
        try {
            await Open();

            long tickBegin = DateTime.Now.Ticks;

            await SendMessageAsync(requestBody, ct);
            Message response = await ReadMessageAsync(ct);

            long tickEnd = DateTime.Now.Ticks;
            long tickDiff = tickEnd - tickBegin;
            double elapsedClientMillis = (double)tickDiff / TimeSpan.TicksPerMillisecond;

            LogSendAndReceive(requestBody, response.Body, elapsedClientMillis, response.Head.elapsedServerMillis);

            if (response.Body is NotAuthorizedResponse) {
                throw new NotAuthorizedException();
            }

            if (response.Body is not T t) {
                throw new Exception(
                    $"Unexpected response type from logic server. Got '{response.Body.GetType().Name}' but expected '{typeof(T).Name}'.");
            }

            return t;
        } catch (SocketException e) {
            Log.Error(e);
            throw;
        } finally {
            Close();
        }
    }

    private static void LogSendAndReceive(object requestBody, object responseBody, double clientMillis, double serverMillis) {
        Log.Info($"{requestBody.GetType().Name} -> {responseBody.GetType().Name} (client elapsed {clientMillis} ms) (server elapsed {serverMillis} ms))");
    }
}

public class NetworkException : Exception {
    public NetworkException(string? message, Exception? innerException) : base(message, innerException) { }
}
