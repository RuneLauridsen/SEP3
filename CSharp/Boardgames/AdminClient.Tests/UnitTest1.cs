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
        client = new TestClient(SIMON);
    }
    
    
    //Update user status
    //Update user 
    //get users approved useres = alle users der har status approves
    //get users waiting for approvel, alle users skal have status pending
    //get account med account id
    
    [Fact]
    public async Task Test_AdminLogin() {
        {
            LoginRequest req = new LoginRequest("Bob", "bobersej", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.True(res.loginSuccessful);
            Assert.Equal("",res.errorReason);
        }
        {
            LoginRequest req = new LoginRequest("Bob", "boberikkesej", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        {
            LoginRequest req = new LoginRequest("Bob", "boberikkesej", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        {
            LoginRequest req = new LoginRequest("Sundar", "sundar", true);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Account awaiting admin approval",res.errorReason);
        }
        
    }

    
}
