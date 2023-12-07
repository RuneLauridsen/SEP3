using System.Security.Claims;

namespace Shared.AuthState;

public class AuthStateInMemory : IAuthState {
    private string _jwt = "";
    private ClaimsPrincipal _claims { get; set; } = new();
    public event EventHandler<string>? AuthStateChanged;

    public async Task<string> GetJwtAsync() {
        return _jwt;
    }

    public async Task<ClaimsPrincipal> GetClaimsAsync() {
        return _claims;
    }

    public async Task SetAuthStateAsync(String jwt) {
        _jwt = jwt;
        _claims = AuthUtil.ParseClaimsFromJwt(jwt);
        AuthStateChanged?.Invoke(this, jwt);
    }
}
