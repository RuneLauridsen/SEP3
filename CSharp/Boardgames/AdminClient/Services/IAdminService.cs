using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;

namespace AdminClient.Services;

public interface IAdminService
{
    public Task<UpdateUserStatusResponse> UpdateUserStatusAsync(Account account, int newStatus);
    public Task<List<Account>> GetUsersWaitingForApprovalAsync();
    public Task<Account> GetAccountAsync(GetAccountReq req);
    public Task<UpdateAccountRes> UpdateAccountAsync(UpdateAccountReq req);
    public Task<IEnumerable<Account>> GetApprovedUsersAsync();
}
