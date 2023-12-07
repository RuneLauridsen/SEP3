using GameClient.Services;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;
using Shared.Tets;

namespace GameClient.Tests;

public class UnitTest1 {
    private readonly IAuthState authState;
    private readonly IGameService gameService;
    private readonly IAuthService authService;

    public UnitTest1() {
        TestUtil.ResetDatabase();

        authState = new AuthStateInMemory();
        authService = new JwtAuthService(authState);
        gameService = new GameService(authState);

        authService.LoginAsync(new LoginRequest("Minii", "simon", false)).Wait();
    }

    // TODO(rune): Test_Login
    // TODO(rune): Test_Register

    [Fact]
    public void Test_GetGames() {
        var req = new GetGamesRequest();
        var res = gameService.GetGamesAsync(req).Result;
        var games = res.games;
        Assert.Equal(2, games.Count);
    }

    // TODO(rune): Test_GetMatch
    // TODO(rune): Test_GetMyMatches
    // TODO(rune): Test_CreateMatch
    // TODO(rune): Test_AddParticipant
    // TODO(rune): Test_GetParticipants
    // TODO(rune): Test_GetPending
    // TODO(rune): Test_DecidePending
    // TODO(rune): Test_GetAccount
    
    [Fact]
    public void Test_GetAccount()
    {
        var req = new GetAccountRequest(1);
        var res = gameService.GetAccountAsync(req).Result;
        var account = res.account;
        Assert.Equal(1, account.AccountId);
        
    }
    // TODO(rune): Test_GetAccounts
    // TODO(rune): Test_UpdateAccount
    // TODO(rune): Test_Move
    // TODO(rune): Test_ImpatientWin
    // TODO(rune): Test_GetScoreSums
    // TODO(rune): Test_GetMatchHistory
    // TODO(rune): Test_BeginLiveUpdate
    // TODO(rune): Test_UpdateUserStatus
}
