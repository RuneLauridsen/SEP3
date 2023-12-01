global using static Shared.Data.Messages;
using System.Runtime.InteropServices.JavaScript;
using GameClient.DTO;

// NOTE(rune): Så vi kan copy-paste fra Java.
using boolean = System.Boolean;

namespace Shared.Data;

// TODO(rune): Req/Res vs. Request/Response

// WARNING(rune): Navne skal matche mellem Java og C#.
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

    public record LoginRequest(String username, String password, boolean AdminClient) {}
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

    public record GetMyMatchesRequest() {}
    public record GetMyMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(int gameId) {}
    public record CreateMatchResponse(String errorReason, Match match) {}

    //
    // Participants
    //

    public record AddParticipantReq(int matchId, int accountId) {} // NOTE(rune): AddParticipant = Send invitatoin.
    public record AddParticipantRes(Participant participant, String errorReason) {}

    public record GetParticipantsReq(int matchId) {}
    public record GetParticipantsRes(List<Participant> participants) {}

    public record GetPendingReq() {}
    public record GetPendingRes(List<Participant> participants) {}

    public record DecidePendingReq(int matchId, int participantId, int status) {}
    public record DecidePendingRes(String errorReason) {}

    //
    // Accounts
    //

    public record GetAccountReq(int accountId) {}
    public record GetAccountRes(Account account) {}

    // TODO(rune): Paging?
    // TODO(rune): Kun hent venner?
    // TODO(rune): Kun hent online?
    // TODO(rune): boolean includePending, server side tjek admin.
    public record GetAccountsReq() {}
    public record GetAccountsRes(List<Account> accounts) {}

    // NOTE(rune): Opdaterer kun profil billede hvis account.profilePicture() != null.
    public record UpdateAccountReq(Account account) { }
    public record UpdateAccountRes(String errorReason) { }

    //
    // Move
    //

    public record MoveReq(int matchId, String moveData) {}
    public record MoveRes(int matchId, String gameData, MoveResult result) {}

    public record ImpatientWinRequest(int matchId) { }
    public record ImpatientWinResponse(int matchId, String errorReason) { }

    //
    // Score
    //

    public record GetScoreSumsRequest(int gameId) { }
    public record GetScoreSumsResponse(List<ScoreSum> sums) { }

    public record GetMatchHistoryRequest(int accountId) { }
    public record GetMatchHistoryResponse(List<MatchScore> scores) { }

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() {}

    //ADMIN
    public record UpdateUserStatusRequest(Account Account, int newStatus);

    public record UpdateUserStatusResponse(boolean b);

}
