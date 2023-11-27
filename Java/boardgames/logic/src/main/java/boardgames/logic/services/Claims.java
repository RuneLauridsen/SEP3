package boardgames.logic.services;

public class Claims {
    private final int accountId;
    private final String username;

    public Claims(int accountId, String username) {
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
