package boardgames.logic.model;

import boardgames.logic.messages.Messages.*;

public interface GameServerModel {
    //
    // Authorization
    //

    public LoginResponse login(LoginRequest req);

    //
    // Spilkataolog
    //

    public GetGamesResponse getGames(GetGamesRequest req, String jwt) throws NotAuthorizedException;

    //
    // Matches
    //

    public GetMatchRes getMatch(GetMatchReq req, String jwt) throws NotAuthorizedException;
    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest, String jwt) throws NotAuthorizedException;
    public CreateMatchResponse createMatch(CreateMatchRequest req, String jwt) throws NotAuthorizedException;

    //
    // Participants
    //

    public AddParticipantRes addParticipant(AddParticipantReq req, String jwt) throws NotAuthorizedException;
    public GetParticipantsRes getParticipants(GetParticipantsReq req, String jwt) throws NotAuthorizedException;
    public GetPendingRes getPending(GetPendingReq req, String jwt) throws NotAuthorizedException;
    public DecidePendingRes decidePending(DecidePendingReq req, String jwt) throws NotAuthorizedException;

    //
    // Move
    //

    public MoveResponse move(MoveRequest req, String jwt) throws NotAuthorizedException;
    public GetAccountsRes getAccounts(GetAccountsReq req, String jwt) throws NotAuthorizedException;
}
