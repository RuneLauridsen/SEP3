using System.Security.Claims;
using Microsoft.Extensions.Configuration;
using Shared.AuthState;
using Shared.Data;

namespace Shared.AuthService;

public class JwtAuthService : IAuthService {
    private readonly IAuthState authState;
    private readonly Config config;

    public JwtAuthService(IAuthState authState, Config config) {
        this.authState = authState;
        this.config = config;
    }

    public async Task<LoginResponse> LoginAsync(LoginRequest req) {
        var socket = new ServiceSocket(config.LogicAddress, config.LogicPort, authState);
        LoginResponse res = await socket.SendAndReceiveAsync<LoginResponse>(req);
        await authState.SetAuthStateAsync(res?.jwt ?? "");
        return res;
    }

    public async Task LogoutAsync() {
        await authState.SetAuthStateAsync("");

    }

    public async Task<RegisterResponse?> RegisterAsync(RegisterRequest req) {
        var socket = new ServiceSocket(config.LogicAddress, config.LogicPort, authState);
        return await socket.SendAndReceiveAsync<RegisterResponse>(req);
    }
}
