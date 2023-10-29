package boardgames.game.model;

import boardgames.shared.dto.Account;
import boardgames.game.messages.Messages.*;

public interface GameServerModel {
    public LoginResponse login(LoginRequest request);
    public MoveResponse move(MoveRequest request, Account account);
    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest, Account account);
}
