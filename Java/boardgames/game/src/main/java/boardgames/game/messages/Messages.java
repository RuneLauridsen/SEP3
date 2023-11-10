package boardgames.game.messages;

import java.util.List;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

// NOTE(rune): Syntax i Java og C# er tæt nok på hinanden til, at vi bare kan
// copy paste nedenstående kode mellem Messages.java og Messages.cs.
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

    public record GetMatchesRequest(String jwt) {}
    public record GetMatchesResponse(List<Match> matches) {}

    public record CreateMatchRequest(String jwt, int gameId) {}
    public record CreateMatchResponse(Match match) {}

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
    // Move
    //

    public record MoveRequest(int matchId, String gameState, String jwt) {}
    public record MoveResponse(int matchId, String gameState, String invalidMoveText) {}

    //
    // Fejlkoder
    //

    public record NotAuthorizedResponse() {}
}
