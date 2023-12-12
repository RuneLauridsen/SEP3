using AdminClient.Services;
using Shared.AuthService;
using Shared.AuthState;

namespace Shared.Tets;

public class TestClient {
    public IAuthState AuthState { get; }
    public IAdminService  AdminService{ get; }
    public IAuthService AuthService { get; }

    public TestClient(TestUser user) {
        Config config = new Config {
            LogicAddress = "localhost",
            LogicPort = 1234
        };

        AuthState = new AuthStateInMemory();
        AuthService = new JwtAuthService(AuthState, config);
        AdminService = new AdminService(AuthState, config);
        AuthService.LoginAsync(new LoginRequest(user.Username, user.Password, true)).Wait();
    }
}
