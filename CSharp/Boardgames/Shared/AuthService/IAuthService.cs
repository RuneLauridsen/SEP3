namespace Shared.AuthService;

public interface IAuthService {
    public Task<LoginResponse> LoginAsync(LoginRequest req);
    public Task LogoutAsync();
    public Task<RegisterResponse?> RegisterAsync(RegisterRequest req);
}
