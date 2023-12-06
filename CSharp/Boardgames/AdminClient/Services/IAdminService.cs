using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;

namespace AdminClient.Services;

public interface IAdminService
{
    public Task<UpdateUserStatusResponse> UpdateUserStatusAsync(Account account, int newStatus);
    public Task<List<Account>> GetUsersWaitingForApprovalAsync();
    public Task<Account> GetAccountAsync(GetAccountRequest request);
    public Task<UpdateAccountResponse> UpdateAccountAsync(UpdateAccountRequest request);
    public Task<IEnumerable<Account>> GetApprovedUsersAsync();
}
