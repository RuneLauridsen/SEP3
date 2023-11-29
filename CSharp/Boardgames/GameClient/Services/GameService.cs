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
    private readonly ServiceSocket _socket;
    private readonly IAuthService _authService;

    public GameService(IAuthService authService) {
        _authService = authService;
        _socket = new ServiceSocket("localhost", 1234);
        _socket.Connect();
        _socket._getJwtFunc = () => _authService.GetJwt();
    }

    public LoginResponse Login(LoginRequest request) {
        return _socket.SendAndReceive<LoginResponse>(request);
    }

    public MoveRes Move(MoveReq req) {
        return _socket.SendAndReceive<MoveRes>(req);
    }

    public GetMatchRes GetMatch(GetMatchReq req) {
        return _socket.SendAndReceive<GetMatchRes>(req);
    }

    public GetMyMatchesResponse GetMyMatches(GetMyMatchesRequest request) {
        return _socket.SendAndReceive<GetMyMatchesResponse>(request);
    }

    public CreateMatchResponse CreateMatch(CreateMatchRequest request) {
        return _socket.SendAndReceive<CreateMatchResponse>(request);
    }

    public GetGamesResponse GetGames(GetGamesRequest request) {
        return _socket.SendAndReceive<GetGamesResponse>(request);
    }

    public GetAccountRes GetAccount(GetAccountReq req) {
        return _socket.SendAndReceive<GetAccountRes>(req);
    }

    public GetAccountsRes GetAccounts(GetAccountsReq req) {
        return _socket.SendAndReceive<GetAccountsRes>(req);
    }

    public AddParticipantRes AddParticipant(AddParticipantReq req) {
        return _socket.SendAndReceive<AddParticipantRes>(req);
    }

    public GetParticipantsRes GetParticipants(GetParticipantsReq req) {
        return _socket.SendAndReceive<GetParticipantsRes>(req);
    }

    public GetPendingRes GetPending(GetPendingReq req) {
        return _socket.SendAndReceive<GetPendingRes>(req);
    }

    public DecidePendingRes DecidePending(DecidePendingReq req) {
        return _socket.SendAndReceive<DecidePendingRes>(req);
    }

    public UpdateAccountRes UpdateAccount(UpdateAccountReq req) {
        return _socket.SendAndReceive<UpdateAccountRes>(req);
    }

    public GetScoreSumsResponse GetScoreSums(GetScoreSumsRequest req) {
        return _socket.SendAndReceive<GetScoreSumsResponse>(req);
    }

    public GetMatchHistoryResponse GetMatchHistory(GetMatchHistoryRequest req) {
        return _socket.SendAndReceive<GetMatchHistoryResponse>(req);
    }
}
