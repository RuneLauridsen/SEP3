package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// NOTE(rune): https://stackoverflow.com/a/76747813
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Participant {
    private int participantId;
    private int participantStatus;
    private int matchId;
    private int accountId;
    private Match match;
    private Account account;

    public static final int PARTICIPANT_STATUS_PENDING = 0;     // NOTE(rune): Venter på svar på invitiation.
    public static final int PARTICIPANT_STATUS_REJECTED = 1;    // NOTE(rune): Invitation afvist.
    public static final int PARTICIPANT_STATUS_ACCEPTED = 2;    // NOTE(rune): Invitation godkendt.
    public static final int PARTICIPANT_STATUS_DONE = 3;        // NOTE(rune): Efter match er startet.

    public Participant() {
    }

    public Participant(int participantId, int participantStatus, int matchId, int accountId) {
        this.participantId = participantId;
        this.matchId = matchId;
        this.accountId = accountId;
        this.participantStatus = participantStatus;
    }

    public int participantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int matchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int accountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Match match() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Account account() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int participantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(int participantStatus) {
        this.participantStatus = participantStatus;
    }
}
