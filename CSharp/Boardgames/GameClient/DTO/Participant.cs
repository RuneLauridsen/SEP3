namespace GameClient.DTO;

public class Participant {
    public required int ParticipantId { get; set;}
    public required int ParticipantStatus { get; set;}
    public required int MatchId { get; set;}
    public required int AccountId { get; set;}
    public Match? Match { get; set;}
    public Account? Account { get; set;}

    public const int PARTICIPANT_STATUS_PENDING = 0;     // NOTE(rune): Venter på svar på invitiation.
    public const int PARTICIPANT_STATUS_REJECTED = 1;    // NOTE(rune): Invitation afvist.
    public const int PARTICIPANT_STATUS_ACCEPTED = 2;    // NOTE(rune): Invitation godkendt.
    public const int PARTICIPANT_STATUS_DONE = 3;        // NOTE(rune): Efter match er startet.
}
