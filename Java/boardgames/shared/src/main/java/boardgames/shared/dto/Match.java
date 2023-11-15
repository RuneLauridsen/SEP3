package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;
import java.util.List;

// NOTE(rune): https://stackoverflow.com/a/76747813
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Match {
    private int matchId;
    private String state;
    private int status;
    private int ownerId;
    private int gameId;
    private LocalDateTime createdOn;

    private List<Participant> participants = List.of();
    private Account owner;
    private Game game;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_ONGOING = 2;
    public static final int STATUS_FINISHED = 3;

    public Match() {
    }

    public Match(int matchId, int status, String state, int ownerId, int gameId, LocalDateTime createdOn) {
        this.matchId = matchId;
        this.status = status;
        this.state = state;
        this.gameId = gameId;
        this.ownerId = ownerId;
        this.createdOn = createdOn;
    }

    public int matchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int status() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String state() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int ownerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int gameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<Participant> participants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Account owner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Game game() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LocalDateTime createdOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "#" + matchId;
    }
}
