package boardgames.game.services;

public class JwtClaims {
    public int accountId;
    public String username;

    public JwtClaims(int accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }
}
