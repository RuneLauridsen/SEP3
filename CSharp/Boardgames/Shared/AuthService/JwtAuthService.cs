using System.Security.Claims;
using System.Text.Json;
using GameClient.Data;
using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;

namespace GameClient.Services;

public class JwtAuthService : IAuthService {
    private ServiceSocket socket;

    public JwtAuthService() {
        socket = new ServiceSocket("localhost", 1234);
        Console.WriteLine("Constructor!");
    }

    private string jwt = "";
    private ClaimsPrincipal _claimsPrincipal = new ClaimsPrincipal();

    private ClaimsPrincipal ClaimsPrincipal {
        get => _claimsPrincipal;
        set => _claimsPrincipal = value;
    }

    public Task<bool> LoginAsync(string username, string password, bool isAdminClient) {
        LoginRequest req = new LoginRequest(username, password, isAdminClient);
        LoginResponse res = socket.SendAndReceive<LoginResponse>(req);

        jwt = res.jwt;
        ClaimsPrincipal = ParseClaimsFromJwt(jwt);

        //Todo, async?
        return Task.FromResult(res.loginSuccessful);
    }

    private static ClaimsPrincipal ParseClaimsFromJwt(string jwt) {
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

    public Task LogoutAsync() {
        jwt = "";
        ClaimsPrincipal = new ClaimsPrincipal();
        return Task.CompletedTask;
    }

    public Task RegisterAsync(string userName, string firstName, string lastName, string email, string password) {
        // Todo Gør ordenlig
        RegisterRequest req = new RegisterRequest(userName, firstName, lastName, email, password);
        RegisterResponse res = socket.SendAndReceive<RegisterResponse>(req);
        return Task.CompletedTask;
    }

    public string GetJwt() {
        return jwt ?? "";
    }

    public ClaimsPrincipal GetClaims() {
        return ClaimsPrincipal;
    }
}
