package boardgames.logic.services;

public class Claims {
    private final int accountId;
    private final String username;
    private final boolean isAdmin;

    public Claims(int accountId, String username, boolean isAdmin) {
        this.accountId = accountId;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public int accountId() { return accountId; }
    public String username() { return username; }
    public boolean isAdmin() { return isAdmin; }
}
