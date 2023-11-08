using static GameClient.Data.Messages;

namespace GameClient.Data;

public class GameService : IGameService {
    private readonly GameServiceSocket _socket;

    public GameService() {
        _socket = new GameServiceSocket("localhost", 1234);

        // TODO(rune): Hardcoded login.
        _socket.Connect();
        _ = Login(new LoginRequest("xdxd_2fast4u_xdxd", "abc123"));
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
}
