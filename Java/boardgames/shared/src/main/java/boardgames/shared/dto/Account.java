package boardgames.shared.dto;

import java.time.LocalDateTime;

public final class Account {
    private int accountId;
    private String username;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDateTime registerDateTime;
    private int accountStatus;

    public static final int ACCOUNT_STATUS_PENDING = 0;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public static final int ACCOUNT_STATUS_ACCEPTED = 1; // NOTE(rune): Bruger er godkendt af admin.
    public static final int ACCOUNT_STATUS_DELETED = 2;  // NOTE(rune): Bruger er fjernet af admin.

    public Account() {
    }

    public Account(int accountId, String username, String firstName, String lastName, String email, LocalDateTime registerDateTime, int accountStatus) {
        this.accountId = accountId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registerDateTime = registerDateTime;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
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
        return new Account(0, "?", "?", "?", "?", LocalDateTime.MIN, ACCOUNT_STATUS_PENDING);
    }
}
