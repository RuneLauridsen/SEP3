using static GameClient.Data.Messages;

namespace GameClient.Data;

//
// TODO(rune):
// Skal vi ikke slette dette lag? Vi gentager alligevel bare hvad der står
// i Messages.cs, så måske skulle interfacet i stedet bare være en enkelt
// uspecific metode, der tager (object request) -> (object response).
//

public class GameService : IGameService {
    private readonly GameServiceSocket _socket;

    public GameService() {
        _socket = new GameServiceSocket("localhost", 1234);
        _socket.Connect();
    }

    public LoginResponse Login(LoginRequest request) {
        return _socket.SendAndReceive<LoginResponse>(request);
    }

    public MoveResponse Move(MoveRequest request) {
        return _socket.SendAndReceive<MoveResponse>(request);
    }

    public GetMatchesResponse GetMatches(GetMatchesRequest request) {
        return _socket.SendAndReceive<GetMatchesResponse>(request);
    }

    public CreateMatchResponse CreateMatch(CreateMatchRequest request) {
        return _socket.SendAndReceive<CreateMatchResponse>(request);
    }

    public GetGamesResponse GetGames(GetGamesRequest request) {
        return _socket.SendAndReceive<GetGamesResponse>(request);
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
}
