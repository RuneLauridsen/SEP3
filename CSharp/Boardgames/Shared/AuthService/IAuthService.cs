namespace Shared.AuthService;

public interface IAuthService {
    public Task<bool> LoginAsync(string username, string password, bool isAdminClient);
    public Task LogoutAsync();
    public Task RegisterAsync(string userName, string firstName, string lastName, string email, string password);
}
