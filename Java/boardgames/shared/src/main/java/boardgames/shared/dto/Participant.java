package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Participant {
    private int participantId;
    private int status;
    private int matchId;
    private int accountId;
    private LocalDateTime createdOn;

    private Match match;
    private Account account;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_PENDING = 1;     // NOTE(rune): Venter på svar på invitiation.
    public static final int STATUS_REJECTED = 2;    // NOTE(rune): Invitation afvist.
    public static final int STATUS_ACCEPTED = 3;    // NOTE(rune): Invitation godkendt.
    public static final int STATUS_DONE = 4;        // NOTE(rune): Efter match er startet.

    public Participant() {
    }

    public Participant(int participantId, int status, int matchId, int accountId, LocalDateTime createdOn) {
        this.participantId = participantId;
        this.matchId = matchId;
        this.accountId = accountId;
        this.status = status;
        this.createdOn = createdOn;
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

}
