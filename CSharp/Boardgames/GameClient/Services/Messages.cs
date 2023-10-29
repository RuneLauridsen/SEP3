global using static GameClient.Data.Messages;
using GameClient.DTO;

namespace GameClient.Data;

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Message.java og Message.cs.
public class Messages {
    public record BasicInfo(String error) { }

    public record LoginRequest(String username, String password) { }

    public record LoginResponse(Account account, BasicInfo info) { }

    public record MoveRequest(int matchId, String gameState) { }

    public record MoveResponse(int matchId, String gameState, String invalidMoveText, BasicInfo info) { }

    public record GetMatchesRequest() { }

    public record GetMatchesResponse(List<Match> matches, BasicInfo info) { }
}
