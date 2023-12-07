using System.Security.Claims;
using Shared.AuthState;
using Shared.Data;

namespace Shared.AuthService;

public class JwtAuthService : IAuthService {
    private readonly IAuthState authState;

    public JwtAuthService(IAuthState authState) {

        this.authState = authState;
    }

    public async Task<LoginResponse> LoginAsync(LoginRequest req) {
        var socket = new ServiceSocket("localhost", 1234, authState);
        LoginResponse res = await socket.SendAndReceiveAsync<LoginResponse>(req);
        await authState.SetAuthStateAsync(res?.jwt ?? "");
        return res;
    }

    public async Task LogoutAsync() {
        await authState.SetAuthStateAsync("");
        
    }

    public async Task<RegisterResponse?> RegisterAsync(RegisterRequest req) {
        var socket = new ServiceSocket("localhost", 1234, authState);
        return await socket.SendAndReceiveAsync<RegisterResponse>(req);
    }
}
