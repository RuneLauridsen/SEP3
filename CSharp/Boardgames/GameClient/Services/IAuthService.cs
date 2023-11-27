using System.Security.Claims;

namespace GameClient.Services;

public interface IAuthService
{
    public Task<bool> LoginAsync(string username, string password);
    public Task LogoutAsync();
    public Task RegisterAsync(string userName, string firstName, string lastName, string email, string password);

    public string GetJwt();
    public ClaimsPrincipal GetClaims();
}
