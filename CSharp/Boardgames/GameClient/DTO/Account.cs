namespace GameClient.DTO;

public class Account {
    public required int AccountId { get; set; }
    public required string Username { get; set; }
    public required int AccountStatus { get; set; }

    public const int ACCOUNT_STATUS_PENDING = 0;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public const int ACCOUNT_STATUS_ACCEPTED = 1; // NOTE(rune): Bruger er godkendt af admin.
    public const int ACCOUNT_STATUS_DELETED = 2;  // NOTE(rune): Bruger er fjernet af admin.
}
