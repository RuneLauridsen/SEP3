namespace GameClient.Data;

public interface IAuthService
{
    public Task<bool> LoginAsync(string username, string password);
    public Task LogoutAsync();
    public Task RegisterAsync(string userName, string firstName, string lastName, string email, string password);

    public string GetJwt();

    //Fra aflevering, måske ikke relavant
    /*public Task<UserCreateResult> RegisterAsync(UserCreate userCreate);
    public Task<ClaimsPrincipal> GetAuthAsync();

    public Action<ClaimsPrincipal> OnAuthStateChanged { get; set; }*/

}
