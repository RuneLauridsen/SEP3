package boardgames.game.messages;

import java.util.List;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Messages.java og Messages.cs.
public class Messages {
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String jwt, Account account) {}

    public record MoveRequest(int matchId, String gameState) {}
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) {}

    public record GetMatchesRequest() {}
    public record GetMatchesResponse(List<Match> matches) {}
}
