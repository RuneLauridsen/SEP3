using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using static Shared.Data.Messages;

namespace GameClient.Services;

//
// TODO(rune):
// Skal vi ikke slette dette lag? Vi gentager alligevel bare hvad der står
// i Messages.cs, så måske skulle interfacet i stedet bare være en enkelt
// uspecific metode, der tager (object request) -> (object response).
//

public class GameService : IGameService {
    private readonly IAuthState _authState;

    public GameService(IAuthState authState) {
        _authState = authState;

    }

    public async Task<LoginResponse> LoginAsync(LoginRequest request) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<LoginResponse>(request);
    }

    public async Task<MoveRes> MoveAsync(MoveReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<MoveRes>(req);
    }

    public async Task<GetMatchRes> GetMatchAsync(GetMatchReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetMatchRes>(req);
    }

    public async Task<GetMyMatchesResponse> GetMyMatchesAsync(GetMyMatchesRequest request) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetMyMatchesResponse>(request);
    }

    public async Task<CreateMatchResponse> CreateMatchAsync(CreateMatchRequest request) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<CreateMatchResponse>(request);
    }

    public async Task<GetGamesResponse> GetGamesAsync(GetGamesRequest request) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetGamesResponse>(request);
    }

    public async Task<GetAccountRes> GetAccountAsync(GetAccountReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetAccountRes>(req);
    }

    public async Task<GetAccountsRes> GetAccountsAsync(GetAccountsReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetAccountsRes>(req);
    }

    public async Task<AddParticipantRes> AddParticipantAsync(AddParticipantReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<AddParticipantRes>(req);
    }

    public async Task<GetParticipantsRes> GetParticipantsAsync(GetParticipantsReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetParticipantsRes>(req);
    }

    public async Task<GetPendingRes> GetPendingAsync(GetPendingReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetPendingRes>(req);
    }

    public async Task<DecidePendingRes> DecidePendingAsync(DecidePendingReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<DecidePendingRes>(req);
    }

    public async Task<UpdateAccountRes> UpdateAccountAsync(UpdateAccountReq req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<UpdateAccountRes>(req);
    }

    public async Task<GetScoreSumsResponse> GetScoreSumsAsync(GetScoreSumsRequest req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetScoreSumsResponse>(req);
    }

    public async Task<GetMatchHistoryResponse> GetMatchHistoryAsync(GetMatchHistoryRequest req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<GetMatchHistoryResponse>(req);
    }

    public async Task<ImpatientWinResponse> ImpatientWinAsync(ImpatientWinRequest req) {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<ImpatientWinResponse>(req);
    }
}
