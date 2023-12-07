using AdminClient.Services;
using Shared.AuthService;
using Shared.AuthState;

namespace Shared.Tets;

public class TestClient {
    public IAuthState AuthState { get; }
    public IAdminService  AdminService{ get; }
    public IAuthService AuthService { get; }

    public TestClient(TestUser user) {
        AuthState = new AuthStateInMemory();
        AuthService = new JwtAuthService(AuthState);
        AdminService = new AdminService(AuthState);
        AuthService.LoginAsync(new LoginRequest(user.Username, user.Password, true)).Wait();
    }
}
