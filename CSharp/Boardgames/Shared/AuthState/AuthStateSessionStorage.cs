using System.Security.Claims;
using Microsoft.AspNetCore.Components.Server.ProtectedBrowserStorage;

namespace Shared.AuthState;

public class AuthStateSessionStorage : IAuthState {
    private bool lookedInSessionStorage = false;
    private string _jwt = "";
    private ClaimsPrincipal _claims { get; set; } = new();
    public event EventHandler<string>? AuthStateChanged;

    private readonly ProtectedSessionStorage sessionStorage;

    public AuthStateSessionStorage(ProtectedSessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public async Task<string> GetJwtAsync() {
        await LoadFromSessionStorage();
        return _jwt;
    }

    public async Task<ClaimsPrincipal> GetClaimsAsync() {
        await LoadFromSessionStorage();
        return _claims;
    }

    private async Task LoadFromSessionStorage() {
        if (!lookedInSessionStorage) {
            try {
                var fromStorage = await sessionStorage.GetAsync<string>("jwt");
                await SetAuthStateAsync(fromStorage.Value ?? "");
                lookedInSessionStorage = true;
            } catch (Exception e) {
                // Ikke nogen alvorlig fejl. Beytder bare at bruger skal logge ind igen ved næste page refresh.
                Console.WriteLine(e);
            }
        }
    }

    public async Task SetAuthStateAsync(String jwt) {
        _jwt = jwt;
        _claims = AuthUtil.ParseClaimsFromJwt(jwt);
        try {
            await sessionStorage.SetAsync("jwt", jwt);
        } catch (Exception e) {
            // Ikke nogen alvorlig fejl. Beytder bare at bruger skal logge ind igen ved næste page refresh.
            Console.WriteLine(e);
        }

        AuthStateChanged?.Invoke(this, jwt);
    }
}
