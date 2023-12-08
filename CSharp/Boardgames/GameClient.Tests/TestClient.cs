using GameClient.Services;
using Shared.AuthService;
using Shared.AuthState;

namespace Shared.Tets;

public class TestClient {
    public IAuthState AuthState { get; }
    public IGameService GameService { get; }
    public IAuthService AuthService { get; }

    public TestClient(TestUser user) {
        Config config = new Config {
            LogicAddress = "localhost",
            LogicPort = 1234
        };

        AuthState = new AuthStateInMemory();
        AuthService = new JwtAuthService(AuthState, config);
        GameService = new GameService(AuthState, config);
        AuthService.LoginAsync(new LoginRequest(user.Username, user.Password, false)).Wait();
    }
}
