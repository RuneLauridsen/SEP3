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

        authService.LoginAsync("Minii", "simon", false).Wait();
    }

    [Fact]
    public void Test1() {
        var req = new GetGamesRequest();
        var res = gameService.GetGamesAsync(req).Result;
        var games = res.games;
        Assert.Equal(2, games.Count);
    }
}
