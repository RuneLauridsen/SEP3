using System.Security.Claims;
using Shared.AuthState;
using Shared.Data;

namespace Shared.AuthService;

public class JwtAuthService : IAuthService {
    private readonly IAuthState authState;

    public JwtAuthService(IAuthState authState) {

        this.authState = authState;
    }

    public async Task<bool> LoginAsync(string username, string password, bool isAdminClient) {
        var socket = new ServiceSocket("localhost", 1234, authState);
        LoginRequest req = new LoginRequest(username, password, isAdminClient);
        LoginResponse? res = await socket.SendAndReceiveAsync<LoginResponse>(req);

        await authState.SetAuthStateAsync(res?.jwt ?? "");

        return res?.loginSuccessful ?? false;
    }

    public async Task LogoutAsync() {
        await authState.SetAuthStateAsync("");
        
    }

    public async Task<RegisterResponse?> RegisterAsync(RegisterRequest req) {
        var socket = new ServiceSocket("localhost", 1234, authState);
        return await socket.SendAndReceiveAsync<RegisterResponse>(req);
    }
}
