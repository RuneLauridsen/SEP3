namespace GameClient.Data;

public interface IAuthService
{
    public Task LoginAsync(string username, string password);
    public Task LogoutAsync();
    
    //Fra aflevering, måske ikke relavant
    /*public Task<UserCreateResult> RegisterAsync(UserCreate userCreate);
    public Task<ClaimsPrincipal> GetAuthAsync();

    public Action<ClaimsPrincipal> OnAuthStateChanged { get; set; }*/
}