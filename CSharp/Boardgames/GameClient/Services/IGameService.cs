using GameClient.DTO;
using static Shared.Data.Messages;

namespace GameClient.Services;

public interface IGameService {
    public Task<LoginResponse> LoginAsync(LoginRequest request);
    public Task<MoveResponse> MoveAsync(MoveRequest request);
    public Task<GetMatchResponse> GetMatchAsync(GetMatchRequest request);
    public Task<GetMyMatchesResponse> GetMyMatchesAsync(GetMyMatchesRequest request);
    public Task<CreateMatchResponse> CreateMatchAsync(CreateMatchRequest request);
    public Task<GetGamesResponse> GetGamesAsync(GetGamesRequest request);
    public Task<GetAccountResponse> GetAccountAsync(GetAccountRequest request);
    public Task<GetAccountsResponse> GetAccountsAsync(GetAccountsRequest request);

    public Task<AddParticipantResponse> AddParticipantAsync(AddParticipantRequest request);
    public Task<GetParticipantsResponse> GetParticipantsAsync(GetParticipantsRequest request);
    public Task<GetPendingResponse> GetPendingAsync(GetPendingRequest request);
    public Task<DecidePendingResponse> DecidePendingAsync(DecidePendingRequest request);
    public Task<UpdateAccountResponse> UpdateAccountAsync(UpdateAccountRequest request);

    public Task<GetScoreSumsResponse> GetScoreSumsAsync(GetScoreSumsRequest req);
    public Task<GetMatchHistoryResponse> GetMatchHistoryAsync(GetMatchHistoryRequest req);
    public Task<ImpatientWinResponse> ImpatientWinAsync(ImpatientWinRequest req);
}
