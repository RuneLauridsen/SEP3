using static Shared.Data.Messages;

namespace GameClient.Services;

//
// TODO(rune):
// Skal vi ikke slette dette lag? Vi gentager alligevel bare hvad der står
// i Messages.cs, så måske skulle interfacet i stedet bare være en enkelt
// uspecific metode, der tager (object request) -> (object response).
//

public interface IGameService {
    public LoginResponse Login(LoginRequest request);
    public MoveRes Move(MoveReq req);
    public GetMatchRes GetMatch(GetMatchReq req);
    public GetMyMatchesResponse GetMyMatches(GetMyMatchesRequest request);
    public CreateMatchResponse CreateMatch(CreateMatchRequest request);
    public GetGamesResponse GetGames(GetGamesRequest request);
    public GetAccountsRes GetAccounts(GetAccountsReq req);

    public AddParticipantRes AddParticipant(AddParticipantReq req);
    public GetParticipantsRes GetParticipants(GetParticipantsReq req);
    public GetPendingRes GetPending(GetPendingReq req);
    public DecidePendingRes DecidePending(DecidePendingReq req);
}
