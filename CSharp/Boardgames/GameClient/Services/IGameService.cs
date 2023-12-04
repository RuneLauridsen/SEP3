using GameClient.DTO;
using static Shared.Data.Messages;

namespace GameClient.Services;

//
// TODO(rune):
// Skal vi ikke slette dette lag? Vi gentager alligevel bare hvad der står
// i Messages.cs, så måske skulle interfacet i stedet bare være en enkelt
// uspecific metode, der tager (object request) -> (object response).
//

public interface IGameService {
    public Task<LoginResponse> LoginAsync(LoginRequest request);
    public Task<MoveRes> MoveAsync(MoveReq req);
    public Task<GetMatchRes> GetMatchAsync(GetMatchReq req);
    public Task<GetMyMatchesResponse> GetMyMatchesAsync(GetMyMatchesRequest request);
    public Task<CreateMatchResponse> CreateMatchAsync(CreateMatchRequest request);
    public Task<GetGamesResponse> GetGamesAsync(GetGamesRequest request);
    public Task<GetAccountRes> GetAccountAsync(GetAccountReq req);
    public Task<GetAccountsRes> GetAccountsAsync(GetAccountsReq req);

    public Task<AddParticipantRes> AddParticipantAsync(AddParticipantReq req);
    public Task<GetParticipantsRes> GetParticipantsAsync(GetParticipantsReq req);
    public Task<GetPendingRes> GetPendingAsync(GetPendingReq req);
    public Task<DecidePendingRes> DecidePendingAsync(DecidePendingReq req);
    public Task<UpdateAccountRes> UpdateAccountAsync(UpdateAccountReq req);

    public Task<GetScoreSumsResponse> GetScoreSumsAsync(GetScoreSumsRequest req);
    public Task<GetMatchHistoryResponse> GetMatchHistoryAsync(GetMatchHistoryRequest req);
    public Task<ImpatientWinResponse> ImpatientWinAsync(ImpatientWinRequest req);
}
