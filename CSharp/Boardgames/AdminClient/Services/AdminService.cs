using GameClient.DTO;
using Shared;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using static Shared.Data.Messages;
namespace AdminClient.Services;

public class AdminService : IAdminService
{
    private readonly IAuthState _authState;
    private readonly Config _config;

    public AdminService(IAuthState authState, Config config) {
        _authState = authState;
        _config = config;
    }

    public async Task<UpdateUserStatusResponse> UpdateUserStatusAsync(Account account, int newStatus)
    {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<UpdateUserStatusResponse>(new UpdateUserStatusRequest(account, newStatus));
    }

    public async Task<List<Account>> GetUsersWaitingForApprovalAsync()
    {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        List<Account> allAccounts = (await socket.SendAndReceiveAsync<GetAccountsResponse>(new GetAccountsRequest())).accounts;
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


    public async Task<Account> GetAccountAsync(GetAccountRequest request)
    {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return (await socket.SendAndReceiveAsync<GetAccountResponse>(request)).account;
    }

    public async Task<UpdateAccountResponse> UpdateAccountAsync(UpdateAccountRequest request)
    {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        return await socket.SendAndReceiveAsync<UpdateAccountResponse>(request);
    }

    public async Task<IEnumerable<Account>> GetApprovedUsersAsync() {
        var socket = new ServiceSocket(_config.LogicAddress, _config.LogicPort, _authState);
        GetAccountsResponse respnse = await socket.SendAndReceiveAsync<GetAccountsResponse>(new GetAccountsRequest());
        List<Account> allAccounts = respnse.accounts;
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
