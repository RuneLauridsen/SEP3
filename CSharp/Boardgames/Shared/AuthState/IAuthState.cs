using System.Security.Claims;

namespace Shared.AuthState;

public interface IAuthState {
    public Task<string> GetJwtAsync();
    public Task<ClaimsPrincipal> GetClaimsAsync();
    public Task SetAuthStateAsync(String jwt);
    public event EventHandler<string>? AuthStateChanged;
}
