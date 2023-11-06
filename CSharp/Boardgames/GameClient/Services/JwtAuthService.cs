using System.Security.Claims;
using System.Text.Json;
using GameClient.DTO;

namespace GameClient.Data;

public class JwtAuthService : IAuthService
{
    private GameServiceSocket socket = new GameServiceSocket("localhost", 1234);
    
    private readonly HttpClient client;
    public static string? Jwt { get; private set; } = "";

    public Action<ClaimsPrincipal> OnAuthStateChanged { get; set; }
    
    //Todo, async?
    public  Task LoginAsync(string username,  string password)
    {
        LoginResponse response = socket.SendAndReceive<LoginResponse>(new LoginRequest(username, password));
        
        
        Jwt = response.jwt;
        
        ClaimsPrincipal principal = CreateClaimsPrincipal();

        OnAuthStateChanged.Invoke(principal);
        
        return Task.CompletedTask;
    }

    private ClaimsPrincipal CreateClaimsPrincipal()
    {
        if (string.IsNullOrEmpty(Jwt))
        {
            return new ClaimsPrincipal();
        }
        
        IEnumerable<Claim> claims = ParseClaimsFromJwt(Jwt);

        ClaimsIdentity identity = new(claims, "jwt");

        ClaimsPrincipal principal = new(identity);
        return principal;
    }

    private IEnumerable<Claim> ParseClaimsFromJwt(string jwt)
    {
        string payload = jwt.Split('.')[1];
        byte[] jsonBytes = ParseBase64WithoutPadding(payload);
        Dictionary<string, object>? keyValuePairs = JsonSerializer.Deserialize<Dictionary<string, object>>(jsonBytes);
        return keyValuePairs!.Select(kvp => new Claim(kvp.Key, kvp.Value.ToString()!));
    }

    private byte[] ParseBase64WithoutPadding(string base64)
    {
        switch (base64.Length % 4)
        {
            case 2:
                base64 += "==";
                break;
            case 3:
                base64 += "=";
                break;
        }

        return Convert.FromBase64String(base64);
    }

    public Task LogoutAsync()
    {
        Jwt = null;
        ClaimsPrincipal principal = new();
        OnAuthStateChanged.Invoke(principal);
        return Task.CompletedTask;
    }

    public Task RegisterAsync(string userName, string firstName, string lastName, string email, string password)
    {
        // Todo Gør ordenlig
        RegisterResponse response =
            socket.SendAndReceive<RegisterResponse>(new RegisterRequest(userName, firstName, lastName, email,
                password));
        return Task.CompletedTask;
    }
}