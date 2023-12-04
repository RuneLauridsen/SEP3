using System.Security.Claims;
using System.Text.Json;

namespace Shared.AuthState;

public static class AuthUtil {
    public static ClaimsPrincipal ParseClaimsFromJwt(string jwt) {
        if (string.IsNullOrEmpty(jwt)) {
            return new ClaimsPrincipal();
        }

        var payload = jwt.Split('.')[1];
        var jsonBytes = ParseBase64WithoutPadding(payload);

        var kvps = JsonSerializer.Deserialize<Dictionary<string, object>>(jsonBytes)!;
        var claims = kvps.Select(kvp => new Claim(kvp.Key, kvp.Value.ToString()!));

        var identity = new ClaimsIdentity(claims, "jwt");
        var principal = new ClaimsPrincipal(identity);
        return principal;
    }

    private static byte[] ParseBase64WithoutPadding(string base64) {
        switch (base64.Length % 4) {
            case 2:
                base64 += "==";
                break;
            case 3:
                base64 += "=";
                break;
        }

        return Convert.FromBase64String(base64);
    }
}
