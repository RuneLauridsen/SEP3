using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;
namespace AdminClient.Services;

public class AdminService : IAdminService
{
    private readonly ServiceSocket _socket;

    public AdminService() {
        _socket = new ServiceSocket("localhost", 1234);
        _socket.Connect();
    }
    public UpdateUserStatusResponse UpdateUserStatus(Account account, int newStatus)
    {
        return _socket.SendAndReceive<UpdateUserStatusResponse>(new UpdateUserStatusRequest(account, newStatus));
    }
    

    public List<Account> GetUsersWaitingForApproval()
    {
        List<Account> allAccounts = _socket.SendAndReceive<GetAccountsRes>(new GetAccountsReq()).accounts;
        List<Account> accountsWaitingForApproval = new List<Account>();
        foreach (Account account in allAccounts)
        {
            if (account.Status == Account.STATUS_PENDING)
            {
                accountsWaitingForApproval.Add(account);
            }
        }
        return accountsWaitingForApproval;
    }

    public List<Account> GetAllUsers()
    {
        return _socket.SendAndReceive<GetAccountsRes>(new GetAccountsReq()).accounts;
    }
}