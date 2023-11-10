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

    public GetGamesResponse getGames(GetGamesRequest req);

    //
    // Matches
    //

    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest);
    public CreateMatchResponse createMatch(CreateMatchRequest req);

    //
    // Participants
    //

    public AddParticipantRes addParticipant(AddParticipantReq req) throws NotAuthorizedException;
    public GetParticipantsRes getParticipants(GetParticipantsReq req);
    public GetPendingRes getPending(GetPendingReq req);
    public DecidePendingRes decidePending(DecidePendingReq req) throws NotAuthorizedException;

    //
    // Move
    //

    public MoveResponse move(MoveRequest request);
}
