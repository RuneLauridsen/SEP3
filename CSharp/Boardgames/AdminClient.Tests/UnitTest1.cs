using GameClient.DTO;
using AdminClient.Services;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using Shared.Tets;

namespace AdminClient.Tests;

public class UnitTest1 {
    private TestClient client;

    public UnitTest1() {
        TestUtil.ResetDatabase();
        client = new TestClient(BOB);
    }
    
    
    //Update user status
    //Update user 
    
    
    //get users approved useres = alle users der har status approves
    [Fact]
    public async Task Test_getAllApprovedUsers()
    {
        List<Account> allApprovedAccounts = (List<Account>)await client.AdminService.GetApprovedUsersAsync();
        Assert.Equal(5,allApprovedAccounts.Count);
        foreach (Account a in allApprovedAccounts)
        {
            Assert.Equal(Account.STATUS_ACCEPTED, a.Status);
        }
        
    }
    
    //get users waiting for approvel, alle users skal have status pending
    [Fact]
    public async Task Test_GetUsersWaitingForApproval()
    {
        List<Account> accountsWaitingForApproval = await client.AdminService.GetUsersWaitingForApprovalAsync();
        Assert.Single(accountsWaitingForApproval);
        foreach (Account a in accountsWaitingForApproval)
        {
            Assert.Equal(Account.STATUS_PENDING, a.Status);
        }
    }
    
    
    //get account med account id
    
    
    //Tester login funktionen i admin client
    [Fact]
    public async Task Test_AdminLogin() {
        {
            //Login med admin bruger 
            LoginRequest req = new LoginRequest("Bob", "bobersej", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.True(res.loginSuccessful);
            Assert.Equal("",res.errorReason);
        }
        {
            //login med admin brugernavn, forkert password
            LoginRequest req = new LoginRequest("Bob", "boberikkesej", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        
        {
            //Login med ikke admin bruger
            LoginRequest req = new LoginRequest("rune", "runerune", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Admin account required for admin client",res.errorReason);
        }
        
        {
            //login med ikke registreret account 
            LoginRequest req = new LoginRequest("Marie", "ikkebruger", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        
    }

    
}
