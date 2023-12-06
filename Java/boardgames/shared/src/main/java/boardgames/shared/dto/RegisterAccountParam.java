package boardgames.shared.dto;

import java.time.LocalDateTime;

public class RegisterAccountParam {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

    public RegisterAccountParam(String username, String firstName, String lastName, String email, String hashedPassword) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
