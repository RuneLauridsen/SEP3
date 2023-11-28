package boardgames.logic.model;

import boardgames.logic.messages.Messages.*;

public interface LogicServerModel {
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
    public GetMyMatchesResponse getMyMatches(GetMyMatchesRequest getMatchesRequest, String jwt) throws NotAuthorizedException;
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

    public MoveRes move(MoveReq req, String jwt) throws NotAuthorizedException;

    //
    // Account
    //

    public GetAccountRes getAccount(GetAccountReq req, String jwt) throws NotAuthorizedException;
    public GetAccountsRes getAccounts(GetAccountsReq req, String jwt) throws NotAuthorizedException;

    public UpdateUserStatusResponse approveUserReg(UpdateUserStatusRequest req, String jwt) throws NotAuthorizedException;
    public UpdateAccountRes updateAccount(UpdateAccountReq req, String jwt) throws NotAuthorizedException;
    public GetScoreSumsResponse getScoreSums(GetScoreSumsRequest req, String jwt);
}
