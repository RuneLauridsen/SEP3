using Shared;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using static Shared.Data.Messages;

namespace GameClient.Services;

public class GameService : IGameService {
    private readonly IAuthState _authState;
    private readonly Config _config;

    public GameService(IAuthState authState, Config config) {
        _authState = authState;
        _config = config;
    }

    public async Task<LoginResponse> LoginAsync(LoginRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<LoginResponse>(request);
    }

    public async Task<MoveResponse> MoveAsync(MoveRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<MoveResponse>(request);
    }

    public async Task<GetMatchResponse> GetMatchAsync(GetMatchRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetMatchResponse>(request);
    }

    public async Task<GetMyMatchesResponse> GetMyMatchesAsync(GetMyMatchesRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetMyMatchesResponse>(request);
    }

    public async Task<CreateMatchResponse> CreateMatchAsync(CreateMatchRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<CreateMatchResponse>(request);
    }

    public async Task<GetGamesResponse> GetGamesAsync(GetGamesRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetGamesResponse>(request);
    }

    public async Task<GetAccountResponse> GetAccountAsync(GetAccountRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetAccountResponse>(request);
    }

    public async Task<GetAccountsResponse> GetAccountsAsync(GetAccountsRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetAccountsResponse>(request);
    }

    public async Task<AddParticipantResponse> AddParticipantAsync(AddParticipantRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<AddParticipantResponse>(request);
    }

    public async Task<GetParticipantsResponse> GetParticipantsAsync(GetParticipantsRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetParticipantsResponse>(request);
    }

    public async Task<GetPendingResponse> GetPendingAsync(GetPendingRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetPendingResponse>(request);
    }

    public async Task<DecidePendingResponse> DecidePendingAsync(DecidePendingRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<DecidePendingResponse>(request);
    }

    public async Task<UpdateAccountResponse> UpdateAccountAsync(UpdateAccountRequest request) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<UpdateAccountResponse>(request);
    }

    public async Task<GetScoreSumsResponse> GetScoreSumsAsync(GetScoreSumsRequest req) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetScoreSumsResponse>(req);
    }

    public async Task<GetMatchHistoryResponse> GetMatchHistoryAsync(GetMatchHistoryRequest req) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<GetMatchHistoryResponse>(req);
    }

    public async Task<ImpatientWinResponse> ImpatientWinAsync(ImpatientWinRequest req) {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<ImpatientWinResponse>(req);
    }
}
