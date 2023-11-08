package boardgames.shared.dto;

public final class Account {
    private int accountId;
    private String username;
    private int accountStatus;

    public static final int ACCOUNT_STATUS_PENDING = 0;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public static final int ACCOUNT_STATUS_ACCEPTED = 1; // NOTE(rune): Bruger er godkendt af admin.
    public static final int ACCOUNT_STATUS_DELETED = 2;  // NOTE(rune): Bruger er fjernet af admin.

    public Account() {
    }

    public Account(int accountId, String username, int accountStatus) {
        this.accountId = accountId;
        this.username = username;
        this.accountStatus = accountStatus;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return username;
    }

    public static Account empty() {
        return new Account(0, "?", ACCOUNT_STATUS_PENDING);
    }
}
