using GameClient.DTO;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using static Shared.Data.Messages;
namespace AdminClient.Services;

public class AdminService : IAdminService
{
    private readonly IAuthState _authState;

    public AdminService(IAuthState authState) {
        _authState = authState;

    }

    public async Task<UpdateUserStatusResponse> UpdateUserStatusAsync(Account account, int newStatus)
    {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<UpdateUserStatusResponse>(new UpdateUserStatusRequest(account, newStatus));
    }

    public async Task<List<Account>> GetUsersWaitingForApprovalAsync()
    {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        List<Account> allAccounts = (await socket.SendAndReceiveAsync<GetAccountsRes>(new GetAccountsReq())).accounts;
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


    public async Task<Account> GetAccountAsync(GetAccountReq req)
    {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return (await socket.SendAndReceiveAsync<GetAccountRes>(req)).account;
    }

    public async Task<UpdateAccountRes> UpdateAccountAsync(UpdateAccountReq req)
    {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        return await socket.SendAndReceiveAsync<UpdateAccountRes>(req);
    }

    public async Task<IEnumerable<Account>> GetApprovedUsersAsync() {
        var socket = new ServiceSocket("localhost", 1234, _authState);
        GetAccountsRes res = await socket.SendAndReceiveAsync<GetAccountsRes>(new GetAccountsReq());
        List<Account> allAccounts = res.accounts;
        List<Account> approvedAccounts = new List<Account>();
        foreach (Account account in allAccounts)
        {
            if (account.Status == Account.STATUS_ACCEPTED)
            {
                approvedAccounts.Add(account);
            }
        }
        return approvedAccounts;
    }
}
