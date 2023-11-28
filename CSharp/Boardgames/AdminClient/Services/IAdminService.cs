using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;

namespace AdminClient.Services;

public interface IAdminService
{
    public UpdateUserStatusResponse UpdateUserStatus(Account account, int newStatus);

    public List<Account> GetUsersWaitingForApproval();
    Account GetAccount(GetAccountReq req);
    UpdateAccountRes UpdateAccount(UpdateAccountReq req);
    IEnumerable<Account> GetApprovedUsers();
}