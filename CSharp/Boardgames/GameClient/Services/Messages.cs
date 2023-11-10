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

// TODO(rune): Req/Res vs. Request/Response

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Message.java og Message.cs.
public class Messages {
    //
    // Authorization
    //

    public record LoginRequest(String username, String password) {}
    public record LoginResponse(boolean loginSuccessful, Account account, String jwt) {}

    public record RegisterRequest(String username, String firstName, String lastName, String email, String password) {}
    public record RegisterResponse(boolean response) {}  //Todo, lav response ordenlig

    //
    // Spilkataolog
    //

    public record GetGamesRequest(String jwt) {}
    public record GetGamesResponse(List<Game> games) {}

    //
    // Matches
    //

    public record GetMatchReq(int matchId, String jwt) {}
    public record GetMatchRes(Match match) {}

    public record GetMatchesRequest(String jwt) {}
    public record GetMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(String jwt, int gameId) {}
    public record CreateMatchResponse(String errorReason, Match match) {}

    //
    // Participants
    //

    public record AddParticipantReq(int matchId, int accountId, String jwt) {} // NOTE(rune): AddParticipant = Send invitatoin.
    public record AddParticipantRes(String errorReason) {}

    public record GetParticipantsReq(int matchId, String jwt) {}
    public record GetParticipantsRes(List<Participant> participants) {}

    public record GetPendingReq(String jwt) {}
    public record GetPendingRes(List<Participant> participants) {}

    public record DecidePendingReq(int participantId, int status, String jwt) {}
    public record DecidePendingRes(String errorReason) {}

    //
    // Accounts
    //

    // TODO(rune): Paging?
    // TODO(rune): Kun hent venner?
    // TODO(rune): Kun hent online?
    public record GetAccountsReq(String jwt) {}
    public record GetAccountsRes(List<Account> accounts) {}

    //
    // Move
    //

    public record MoveRequest(int matchId, String gameState, String jwt) {}
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) {}

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() {}
}
