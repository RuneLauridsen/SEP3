using System.Security.Claims;

namespace Shared.AuthState;

public interface IAuthState {
    public Task<string> GetJwtAsync();
    public Task<ClaimsPrincipal> GetClaimsAsync();
    public Task SetAuthStateAsync(String jwt);
    public event EventHandler<string>? AuthStateChanged;
}

public static class AuthStateExtensions {
    public static async Task<int> GetUserIdAsync(this IAuthState a) {
        ClaimsPrincipal claims = await a.GetClaimsAsync();
        string userIdAsString = claims.GetOrDefault("userId", "");
        int userIdAsInt = ParseUtil.ParseIntOrDefault(userIdAsString, 0);
        return userIdAsInt;
    }

    public static async Task<string> GetUsernameAsync(this IAuthState a) {
        ClaimsPrincipal claims = await a.GetClaimsAsync();
        string username = claims.GetOrDefault("username", "?");
        return username;
    }
}
