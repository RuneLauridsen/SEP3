namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class Participant {
    public required int ParticipantId { get; set;}
    public required int Status { get; set;}
    public required int MatchId { get; set;}
    public required int AccountId { get; set;}
    public required DateTime CreatedOn { get; set; }
    public Match Match { get; set; } = Empty.Match();
    public Account Account { get; set; } = Empty.Account();
    public int Score { get; set; } = 0;

    public const int STATUS_NONE = 0;
    public const int STATUS_PENDING = 1;     // NOTE(rune): Venter på svar på invitiation.
    public const int STATUS_REJECTED = 2;    // NOTE(rune): Invitation afvist.
    public const int STATUS_ACCEPTED = 3;    // NOTE(rune): Invitation godkendt.
    public const int STATUS_FINISHED = 4;    // NOTE(rune): Efter match er færdig.

    public string StatusName() {
        return Status switch {
            STATUS_NONE => "STATUS_NONE",
            STATUS_PENDING => "STATUS_PENDING",
            STATUS_REJECTED => "STATUS_REJECTED",
            STATUS_ACCEPTED => "STATUS_ACCEPTED",
            STATUS_FINISHED => "STATUS_FINISHED",
        };
    }
}
