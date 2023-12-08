using Shared;
using Shared.AuthState;
using Shared.Data;

namespace GameClient.Services;

public class LiveService : ILiveService, IDisposable {
    public event EventHandler<int>? MatchUpdated;

    private readonly IAuthState _authState;
    private readonly Config _config;
    private Task? _task;

    private CancellationTokenSource _cts = new();

    private bool _hasSentLiveRequest;

    public LiveService(IAuthState authState, Config config) {
        _authState = authState;
        _config = config;
        _authState.AuthStateChanged += (s, e) => {
            _cts.Cancel();
            _cts = new();
            _task = LiveUpdateTask(_cts.Token);
        };
    }

    public async Task LiveUpdateTask(CancellationToken ct) {
        ServiceSocket socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        try {
            await socket.Open();

            // Initial live update request.
            {
                var req = new BeginLiveUpdateRequest();
                await socket.SendMessageAsync(req, ct);
                Message res = await socket.ReadMessageAsync(ct);
                if (res.Body is not BeginLiveUpdateResponse { success: true }) {
                    return;
                }
            }

            // Wait for notifications.
            while (!ct.IsCancellationRequested) {
                Message m = await socket.ReadMessageAsync(ct);
                if (m.Body is MatchNotification not) {
                    MatchUpdated?.Invoke(this, not.matchId);
                }
            }
        } catch (Exception e) {
            // Cancelled.
        } finally {
            socket.Close();
        }
    }

    public void Dispose() {
        _cts.Cancel();
    }
}
