package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public final class Account {
    private int accountId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime registerDateTime;
    private LocalDateTime createdOn;
    private String profilePicture;     // NOTE(rune): Som base 64
    private String profilePictureType; // NOTE(rune): MIME content type.
    private int status;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_PENDING = 1;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public static final int STATUS_ACCEPTED = 2; // NOTE(rune): Bruger er godkendt af admin.
    public static final int STATUS_DELETED = 3;  // NOTE(rune): Bruger er fjernet af admin.

    public Account() {
    }

    public Account(int accountId, String username, String firstName, String lastName, String email, LocalDateTime registerDateTime, int status, LocalDateTime createdOn) {
        this.accountId = accountId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registerDateTime = registerDateTime;
        this.status = status;
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

    public int status() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime createdOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String profilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String profilePictureType() {
        return profilePictureType;
    }

    public void setProfilePictureType(String profilePictureType) {
        this.profilePictureType = profilePictureType;
    }

    @Override
    public String toString() {
        return username;
    }
}
