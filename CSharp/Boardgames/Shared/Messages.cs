global using static Shared.Data.Messages;
using System.Runtime.InteropServices.JavaScript;
using GameClient.DTO;

// NOTE(rune): Så vi kan copy-paste fra Java.
using boolean = System.Boolean;

namespace Shared.Data;

// TODO(rune): Req/Res vs. Request/Response

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Messages.java og Messages.cs.
public class Messages {

    // NOTE(rune): Baisc info som sendes sammen med alle messages.
    // - bodyType:            Udfyldes af både client og server.
    // - jwt:                 Udfyldes kun af client
    // - elapsedServerMillis: Udfyldes kun af server.
    public record Head(String bodyType, String jwt, double elapsedServerMillis) {}

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

    public record GetGamesRequest() {}
    public record GetGamesResponse(List<Game> games) {}

    //
    // Matches
    //

    public record GetMatchReq(int matchId) {}
    public record GetMatchRes(Match match) {}

    public record GetMatchesRequest() {}
    public record GetMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(int gameId) {}
    public record CreateMatchResponse(String errorReason, Match match) {}

    //
    // Participants
    //

    public record AddParticipantReq(int matchId, int accountId) {} // NOTE(rune): AddParticipant = Send invitatoin.
    public record AddParticipantRes(String errorReason) {}

    public record GetParticipantsReq(int matchId) {}
    public record GetParticipantsRes(List<Participant> participants) {}

    public record GetPendingReq() {}
    public record GetPendingRes(List<Participant> participants) {}

    public record DecidePendingReq(int participantId, int status) {}
    public record DecidePendingRes(String errorReason) {}

    //
    // Accounts
    //

    // TODO(rune): Paging?
    // TODO(rune): Kun hent venner?
    // TODO(rune): Kun hent online?
    public record GetAccountsReq() {}
    public record GetAccountsRes(List<Account> accounts) {}

    //
    // Move
    //

    public record MoveRequest(int matchId, String gameState) {}
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) {}

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() {}

    //ADMIN
    public record ApproveUserRequest(Account Account);

    public record ApproveUserResponse(boolean b);
    public record RejectUserRequest(Account Account);

    public record RejectUserResponse(boolean b);

    public record GetUsersWaitingForApprovalRequest();
    public record GetUsersWaitingForApprovalResponse(List<Account> Members);
}
