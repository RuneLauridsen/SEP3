package boardgames.shared.dto;

public class Participant {
    private int participantId;
    private Account account;
    private int participantStatus;

    public static final int PARTICIPANT_STATUS_PENDING = 0;     // NOTE(rune): Venter på svar på invitiation.
    public static final int PARTICIPANT_STATUS_REJECTED = 1;    // NOTE(rune): Invitation afvist.
    public static final int PARTICIPANT_STATUS_ACCEPTED = 2;    // NOTE(rune): Invitation godkendt.

    public Participant() {
    }

    public Participant(int participantId, Account account, int participantStatus) {
        this.participantId = participantId;
        this.account = account;
        this.participantStatus = participantStatus;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getParticipantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(int participantStatus) {
        this.participantStatus = participantStatus;
    }
}
