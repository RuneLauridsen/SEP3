package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

// NOTE(rune): https://stackoverflow.com/a/76747813
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public final class Account {
    private int accountId;
    private String username;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDateTime registerDateTime;
    public LocalDateTime createdOn;
    private int accountStatus;

    public static final int ACCOUNT_STATUS_NONE = 0;
    public static final int ACCOUNT_STATUS_PENDING = 1;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public static final int ACCOUNT_STATUS_ACCEPTED = 2; // NOTE(rune): Bruger er godkendt af admin.
    public static final int ACCOUNT_STATUS_DELETED = 3;  // NOTE(rune): Bruger er fjernet af admin.

    public Account() {
    }

    public Account(int accountId, String username, String firstName, String lastName, String email, LocalDateTime registerDateTime, int accountStatus, LocalDateTime createdOn) {
        this.accountId = accountId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registerDateTime = registerDateTime;
        this.accountStatus = accountStatus;
        this.createdOn = createdOn;
    }

    public int accountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String username() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String lastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime registerDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public int accountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDateTime createdOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return username;
    }
}
