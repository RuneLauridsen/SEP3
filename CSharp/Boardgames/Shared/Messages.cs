global using static Shared.Data.Messages;
using System.Runtime.InteropServices.JavaScript;
using GameClient.DTO;

// NOTE(rune): Så vi kan copy-paste fra Java.
using boolean = System.Boolean;

namespace Shared.Data;

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

    public record LoginRequest(String username, String password, boolean adminClient) {}
    public record LoginResponse(boolean loginSuccessful, Account account, String jwt) {}

    public record RegisterRequest(String username, String firstName, String lastName, String email, String password) {}
    public record RegisterResponse(boolean response, String errorReason) {}


    //
    // Spilkataolog
    //

    public record GetGamesRequest() {}
    public record GetGamesResponse(List<Game> games) {}

    //
    // Matches
    //

    public record GetMatchRequest(int matchId) {}
    public record GetMatchResponse(Match match) {}

    public record GetMyMatchesRequest() {}
    public record GetMyMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(int gameId) {}
    public record CreateMatchResponse(String errorReason, Match match) {}

    //
    // Participants
    //

    public record AddParticipantRequest(int matchId, int accountId) {} // NOTE(rune): AddParticipant = Send invitatoin.
    public record AddParticipantResponse(Participant participant, String errorReason) {}

    public record GetParticipantsRequest(int matchId) {}
    public record GetParticipantsResponse(List<Participant> participants) {}

    public record GetPendingRequest() {}
    public record GetPendingResponse(List<Participant> participants) {}

    public record DecidePendingRequest(int matchId, int participantId, int status) {}
    public record DecidePendingResponse(String errorReason) {}

    //
    // Accounts
    //

    public record GetAccountRequest(int accountId) {}
    public record GetAccountResponse(Account account) {}

    // TODO(rune): Paging?
    public record GetAccountsRequest() {}
    public record GetAccountsResponse(List<Account> accounts) {}

    // NOTE(rune): Opdaterer kun profil billede hvis account.profilePicture() != null.
    public record UpdateAccountRequest(Account account) { }
    public record UpdateAccountResponse(String errorReason) { }

    //
    // Move
    //

    public record MoveRequest(int matchId, String moveData) {}
    public record MoveResponse(int matchId, String gameData, MoveResult result) {}

    public record ImpatientWinRequest(int matchId) { }
    public record ImpatientWinResponse(int matchId, String errorReason) { }

    //
    // Score
    //

    public record GetScoreSumsRequest(int gameId) { }
    public record GetScoreSumsResponse(List<ScoreSum> sums) { }

    public record GetMatchHistoryRequest(int accountId) { }
    public record GetMatchHistoryResponse(List<FinishedMatchScore> scores) { }

    //
    // Live update
    //

    public record BeginLiveUpdateRequest() { }
    public record BeginLiveUpdateResponse(boolean success) { }
    public record MatchNotification(int matchId) { }
    public record QuitNotification() { }

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() {}

    //ADMIN
    public record UpdateUserStatusRequest(Account Account, int newStatus);

    public record UpdateUserStatusResponse(boolean b);

}
