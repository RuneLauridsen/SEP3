using GameClient.Services;
using Shared.AuthService;
using Shared.AuthState;

namespace Shared.Tets;

public class TestClient {
    public IAuthState AuthState { get; }
    public IGameService GameService { get; }
    public IAuthService AuthService { get; }

    public TestClient(TestUser user) {
        AuthState = new AuthStateInMemory();
        AuthService = new JwtAuthService(AuthState);
        GameService = new GameService(AuthState);
        AuthService.LoginAsync(new LoginRequest(user.Username, user.Password, false)).Wait();
    }
}
