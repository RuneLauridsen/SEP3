package boardgames.shared.dto;

public class Participant {
    private int participantId;
    private Account account;
    private boolean accepted;
    private boolean rejected;

    public Participant() {
    }

    public Participant(int participantId, Account account, boolean accepted, boolean rejected) {
        this.participantId = participantId;
        this.account = account;
        this.accepted = accepted;
        this.rejected = rejected;
    }

    public int getParticipantId() {
        return participantId;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRejected() {
        return rejected;
    }
}
