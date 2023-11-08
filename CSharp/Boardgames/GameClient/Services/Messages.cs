global using static GameClient.Data.Messages;
using System.Runtime.InteropServices.JavaScript;
using GameClient.DTO;

// NOTE(rune): Så vi kan copy-paste fra Java.
using boolean = System.Boolean;

namespace GameClient.Data;

// TODO(rune): Måske gøre JWT til en del af protokolonne, så alle request typerne ikke behøver at construct'es med en JWT?
// TODO(rune): Måske gøre JWT til en del af protokolonne, så alle request typerne ikke behøver at construct'es med en JWT?
// TODO(rune): Måske gøre JWT til en del af protokolonne, så alle request typerne ikke behøver at construct'es med en JWT?
// TODO(rune): Måske gøre JWT til en del af protokolonne, så alle request typerne ikke behøver at construct'es med en JWT?
// TODO(rune): Måske gøre JWT til en del af protokolonne, så alle request typerne ikke behøver at construct'es med en JWT?

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Message.java og Message.cs.
public class Messages {
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(boolean loginSuccessful, Account account, String jwt) {}

    public record RegisterRequest(String username, String firstName, String lastName, String email, String password) {}

    //Todo, lav response ordenlig
    public record RegisterResponse(boolean response) {}

    public record MoveRequest(int matchId, String gameState, String jwt) {}
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) {}

    public record GetMatchesRequest(String jwt) {}
    public record GetMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(String jwt, int gameId) {}
    public record CreateMatchResponse(Match match) {}

    public record GetGamesRequest(String jwt) {}
    public record GetGamesResponse(List<Game> games) {}

    public record NotAuthorizedResponse() {}
}
