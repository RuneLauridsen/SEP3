namespace GameClient.DTO;

public class Account {
    public required int AccountId { get; set; }
    public required string Username { get; set; }
    public required string FirstName { get; set; }
    public required string LastName { get; set; }
    public required string Email { get; set; }
    public required int AccountStatus { get; set; }
    public required DateTime RegisterDateTime { get; set; }
    public required DateTime CreatedOn { get; set; }

    public const int ACCOUNT_STATUS_NONE = 0;
    public const int ACCOUNT_STATUS_PENDING = 1;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public const int ACCOUNT_STATUS_ACCEPTED = 2; // NOTE(rune): Bruger er godkendt af admin.
    public const int ACCOUNT_STATUS_DELETED = 3;  // NOTE(rune): Bruger er fjernet af admin.
}
