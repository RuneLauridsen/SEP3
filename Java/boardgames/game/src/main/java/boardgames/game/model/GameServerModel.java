package boardgames.game.model;

import boardgames.shared.dto.Account;
import boardgames.game.messages.Messages.*;

public interface GameServerModel {
    //
    // Authorization
    //

    public LoginResponse login(LoginRequest request);

    //
    // Spilkataolog
    //

    public GetGamesResponse getGames(GetGamesRequest req) throws NotAuthorizedException;

    //
    // Matches
    //

    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest) throws NotAuthorizedException;
    public CreateMatchResponse createMatch(CreateMatchRequest req) throws NotAuthorizedException;

    //
    // Participants
    //

    public AddParticipantRes addParticipant(AddParticipantReq req) throws NotAuthorizedException;
    public GetParticipantsRes getParticipants(GetParticipantsReq req) throws NotAuthorizedException;
    public GetPendingRes getPending(GetPendingReq req) throws NotAuthorizedException;
    public DecidePendingRes decidePending(DecidePendingReq req) throws NotAuthorizedException;

    //
    // Move
    //

    public MoveResponse move(MoveRequest request) throws NotAuthorizedException;
}
