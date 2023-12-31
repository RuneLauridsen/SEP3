using GameClient.DTO;
using GameClient.Services;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using Shared.Tets;

namespace GameClient.Tests;

public class GameClientTest {
    private TestClient client;

    public GameClientTest() {
        TestUtil.ResetDatabase();
        client = new TestClient(SIMON);
    }

    [Fact]
    public async Task Test_Login()
    {
        {
            //Login med ikke admin bruger
            LoginRequest req = new LoginRequest("rune", "runerune", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.True(res.loginSuccessful);
            Assert.Equal("",res.errorReason);
        }
        {
            //Login med admin bruger 
            LoginRequest req = new LoginRequest("Bob", "bobersej", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.True(res.loginSuccessful);
            Assert.Equal("",res.errorReason);
        }
        {
            //login med rigtig brugernavn, forkert password
            LoginRequest req = new LoginRequest("runerune", "boberikkesej", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        
        {
            //login med ikke registreret account 
            LoginRequest req = new LoginRequest("Marie", "ikkebruger", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Can't find account, invalid credentials",res.errorReason);
        }
        {
            //login med registreret bruger ikke godkendt af admin account 
            LoginRequest req = new LoginRequest("Sundar", "sundar", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Account awaiting admin approval",res.errorReason);
        }
        {
            //login med registreret bruger ikke godkendt af admin account 
            LoginRequest req = new LoginRequest("ToBeDeleted", "ToBeDeleted", false);
            LoginResponse res = await client.AuthService.LoginAsync(req);
            Assert.False(res.loginSuccessful);
            Assert.Equal("Account is no longer active",res.errorReason);
        }
        
    }

    [Fact]
    public async Task Test_register()
    {
        {
            RegisterRequest req = new RegisterRequest("BenDover", "firstName", "lastName", "email", "password");
            RegisterResponse res = await client.AuthService.RegisterAsync(req);
            Assert.False(res.response);
            Assert.Equal("Username Already taken",res.errorReason);
        }
        {
            RegisterRequest req = new RegisterRequest("", "", "", "", "");
            RegisterResponse res = await client.AuthService.RegisterAsync(req);
            Assert.False(res.response);
            Assert.Equal("No parameters can be empty",res.errorReason);
        }
        {
            RegisterRequest req = new RegisterRequest("username", "firstName", "lastName", "email", "password");
            RegisterResponse res = await client.AuthService.RegisterAsync(req);
            Assert.True(res.response);
            Assert.Equal("",res.errorReason);
        }
        
    }
    
    [Fact]
    public void Test_GetGames() {
        var res = client.GameService.GetGamesAsync(new()).Result;
        var games = res.games;
        Assert.Equal(2, games.Count);
    }

    [Fact]
    public void Test_CreateMatch_GetMatch() {
        int matchId;

        {
            var res = client.GameService.CreateMatchAsync(new(TICTACTOE_ID)).Result;
            Assert.Equal(res.errorReason, "");
            Assert.NotEqual(res.match.MatchId, 0);
            matchId = res.match.MatchId;
        }

        {
            var res = client.GameService.GetMatchAsync(new(matchId)).Result;
            var m = res.match;

            Assert.Equal(m.MatchId, matchId);
            Assert.Equal(m.GameId, TICTACTOE_ID);
            Assert.Equal(m.OwnerId, SIMON_ID);
            Assert.Equal(m.Participants.Count, 1);
            Assert.Equal(m.Participants[0].AccountId, SIMON_ID);
            Assert.Equal(m.Participants[0].Status, Participant.STATUS_ACCEPTED);
        }
    }

    [Fact]
    public void Test_CreateMatch_GameNotFound() {
        var res = client.GameService.CreateMatchAsync(new(93718239)).Result;
        Assert.Equal(res.match.MatchId, 0);
        Assert.Equal(res.errorReason, "Game id 93718239 not found.");
    }

    [Fact]
    public async Task Test_GetMyMatches() {
        await client.GameService.CreateMatchAsync(new(TICTACTOE_ID));
        await client.GameService.CreateMatchAsync(new(TICTACTOE_ID));
        await client.GameService.CreateMatchAsync(new(TICTACTOE_ID));

        var res = await client.GameService.GetMyMatchesAsync(new());

        Assert.Equal(3, res.matches.Count);
    }

    [Fact]
    public async Task Test_AddParticipant_GetParticipants() {
        int matchId;
        Match before;
        Match after;

        {
            // Create match.
            var res = await client.GameService.CreateMatchAsync(new(TICTACTOE_ID));
            var m = res.match;
            matchId = m.MatchId;
        }

        {
            // Get match before add participant.
            var res = await client.GameService.GetMatchAsync(new(matchId));
            before = res.match;
        }

        {
            // Add participant.
            var res = await client.GameService.AddParticipantAsync(new(matchId, RUNE_ID));
        }

        {
            // Get match after add participant.
            var res = await client.GameService.GetMatchAsync(new(matchId));
            after = res.match;
        }

        Assert.Equal(before.Participants.Count, 1);
        Assert.Equal(after.Participants.Count, 2);
        Assert.Equal(after.Participants[0].Status, Participant.STATUS_ACCEPTED);
        Assert.Equal(after.Participants[1].Status, Participant.STATUS_PENDING);
        Assert.Equal(after.Participants[0].AccountId, SIMON_ID);
        Assert.Equal(after.Participants[1].AccountId, RUNE_ID);
    }

    [Fact]
    private async Task Test_AddParticipant_InviteSelf() {
        int matchId;
        {
            // Create match.
            var res = await client.GameService.CreateMatchAsync(new(TICTACTOE_ID));
            var m = res.match;
            matchId = m.MatchId;
        }

        {
            // Add participant.
            var res = await client.GameService.AddParticipantAsync(new(matchId, SIMON_ID));
            Assert.Equal(res.errorReason, "Account id " + SIMON_ID + " is already invited.");
        }
    }

    // TODO(rune): Test_GetPending
    // TODO(rune): Test_DecidePending

    [Fact]
    public void Test_GetAccount() {
        var req = new GetAccountRequest(1);
        var res = client.GameService.GetAccountAsync(req).Result;
        var account = res.account;
        Assert.Equal(1, account.AccountId);
    }

    [Fact]
    public void Test_GetNonExistentAccount()
    {
        var req = new GetAccountRequest(999);
        var res = client.GameService.GetAccountAsync(req).Result;
        var account = res.account;
        Assert.Equal(0, account.AccountId);
        Assert.Equal("?", account.Username);
    }

    [Fact]
    public void Test_GetAccounts()
    {
        var req = new GetAccountsRequest();
        var res = client.GameService.GetAccountsAsync(req).Result;
        var accounts = res.accounts.OrderBy(x => x.AccountId).ToList();
        Assert.Equal(5, accounts.Count);
        Assert.Equal(SIMON_ID, accounts[2].AccountId);
    }

    [Fact]
    public void Test_UpdateAccount()
    {
        var req = new GetAccountRequest(SIMON_ID);
        var res = client.GameService.GetAccountAsync(req).Result;
        var bimon = res.account;
        Assert.Equal("Banh", bimon.LastName);

        bimon.LastName = "Mai";
        var upReq = new UpdateAccountRequest(bimon);
        var upRes = client.GameService.UpdateAccountAsync(upReq).Result;
        Assert.Equal("", upRes.errorReason);
        
        var uppedReq = new GetAccountRequest(SIMON_ID);
        var uppedRes = client.GameService.GetAccountAsync(uppedReq).Result;
        var uppedBimon = uppedRes.account;
        Assert.Equal("Mai", uppedBimon.LastName);

    }
    // TODO(rune): Test_Move
    // TODO(rune): Test_GetScoreSums
    // TODO(rune): Test_GetMatchHistory
}
