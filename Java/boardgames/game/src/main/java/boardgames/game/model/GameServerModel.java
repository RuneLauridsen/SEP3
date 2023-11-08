package boardgames.game.model;

import boardgames.game.messages.Messages;
import boardgames.shared.dto.Account;
import boardgames.game.messages.Messages.*;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;

import java.util.List;

public interface GameServerModel {
    public LoginResponse login(LoginRequest request);
    public MoveResponse move(MoveRequest request, Account account);
    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest, Account account); // TODO(rune): Fjern account param, hvis bruger jwt nu.

    public CreateMatchResponse createMatch(CreateMatchRequest req);
    public GetGamesResponse getGames(GetGamesRequest req);
}
