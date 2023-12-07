using GameClient.DTO;
using AdminClient.Services;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using Shared.Tets;

namespace GameClient.Tests;

public class UnitTest1 {
    private TestClient client;

    public UnitTest1() {
        TestUtil.ResetDatabase();
        client = new TestClient(SIMON);
    }
    
    //Login = kun admin brugere må logge ind, kun brugere der findes må logge ind, Password skal være rigtig
    
    //Update user status
    //Update user 
    //get users approved useres = alle users der har status approves
    //get users waiting for approvel, alle users skal have status pending
    //get account med account id

    
    [Fact]
    public void Test_AdminLogin() {
        var res = client.AdminService.
        var games = res.games;
        Assert.Equal(2, games.Count);
    }

    
}
