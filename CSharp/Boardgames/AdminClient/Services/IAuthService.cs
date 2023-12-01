using System.Security.Claims;

namespace GameClient.Services;

public interface IAuthService
{
    public Task<bool> LoginAsync(string username, string password);
    public Task LogoutAsync();

    public string GetJwt();
    public ClaimsPrincipal GetClaims();
}
