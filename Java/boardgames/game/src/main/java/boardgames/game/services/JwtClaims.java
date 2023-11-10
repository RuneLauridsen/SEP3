package boardgames.game.services;

public class JwtClaims {
    private final int accountId;
    private final String username;

    public JwtClaims(int accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public int accountId() {
        return accountId;
    }

    public String username() {
        return username;
    }
}
