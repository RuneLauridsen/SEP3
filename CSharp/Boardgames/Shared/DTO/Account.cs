﻿namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class Account {
    public required int AccountId { get; set; }
    public required string Username { get; set; }
    public required string FirstName { get; set; }
    public required string LastName { get; set; }
    public required string Email { get; set; }
    public required string Description { get; set; }
    public required int Status { get; set; }
    public DateTime RegisterDateTime { get; set; }
    public required DateTime CreatedOn { get; set; }

    public String? ProfilePicture { get; set; }     // NOTE(rune): Som base 64
    public String? ProfilePictureType { get; set; } // NOTE(rune): MIME content type.

    public const int STATUS_NONE = 0;
    public const int STATUS_PENDING = 1;  // NOTE(rune): Bruger ikke godkendt af admin endnu.
    public const int STATUS_ACCEPTED = 2; // NOTE(rune): Bruger er godkendt af admin.
    public const int STATUS_DELETED = 3;  // NOTE(rune): Bruger er fjernet af admin.

    public string StatusName() {
        return Status switch {
            STATUS_NONE => "STATUS_NONE",
            STATUS_PENDING => "STATUS_PENDING",
            STATUS_ACCEPTED => "STATUS_ACCEPTED",
            STATUS_DELETED => "STATUS_DELETED",
            _ => ""
        };
    }
}
