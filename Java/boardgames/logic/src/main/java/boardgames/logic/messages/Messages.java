package boardgames.logic.messages;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;
import boardgames.shared.dto.Participant;

import java.util.List;

// WARNING(rune): Navne skal matche mellem Java og C#.
// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Messages.java og Messages.cs.
public class Messages {

    // NOTE(rune): Baisc info som sendes sammen med alle messages.
    // - bodyType:            Udfyldes af både client og server.
    // - jwt:                 Udfyldes kun af client
    // - elapsedServerMillis: Udfyldes kun af server.
    public record Head(String bodyType, String jwt, double elapsedServerMillis) { }

    //
    // Authorization
    //

    public record LoginRequest(String username, String password) { }
    public record LoginResponse(boolean loginSuccessful, Account account, String jwt) { }

    public record RegisterRequest(String username, String firstName, String lastName, String email, String password) { }
    public record RegisterResponse(boolean response) { }  //Todo, lav response ordenlig

    //
    // Admin Register stuff
    //

    public record UpdateUserStatusRequest(Account account, int newStatus){};
    public record UpdateUserStatusResponse(boolean b){};

    //
    // Spilkataolog
    //

    public record GetGamesRequest() { }
    public record GetGamesResponse(List<Game> games) { }

    //
    // Matches
    //

    public record GetMatchReq(int matchId) { }
    public record GetMatchRes(Match match) { }

    public record GetMyMatchesRequest() { }
    public record GetMyMatchesResponse(List<Match> matches) { }

    public record CreateMatchRequest(int gameId) { }
    public record CreateMatchResponse(String errorReason, Match match) { }

    //
    // Participants
    //

    public record AddParticipantReq(int matchId, int accountId) { } // NOTE(rune): AddParticipant = Send invitatoin.
    public record AddParticipantRes(Participant participant, String errorReason) { }

    public record GetParticipantsReq(int matchId) { }
    public record GetParticipantsRes(List<Participant> participants) { }

    public record GetPendingReq() { }
    public record GetPendingRes(List<Participant> participants) { }

    public record DecidePendingReq(int matchId, int participantId, int status) { }
    public record DecidePendingRes(String errorReason) { }

    //
    // Accounts
    //

    public record GetAccountReq(int accountId) { }
    public record GetAccountRes(Account account) { }

    // TODO(rune): Paging?
    // TODO(rune): Kun hent venner?
    // TODO(rune): Kun hent online?
    public record GetAccountsReq() { }
    public record GetAccountsRes(List<Account> accounts) { }

    //
    // Move
    //

    public record MoveReq(int matchId, String moveData) { }
    public record MoveRes(int matchId, String gameData, MoveResult result) { }

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() { }
}
