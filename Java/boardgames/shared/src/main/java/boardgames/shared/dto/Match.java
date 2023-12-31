package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;
import java.util.List;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.ANY,
    setterVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Match {
    private int matchId;
    private Integer nextAccountId;
    private String data;
    private int status;
    private int ownerId;
    private int gameId;
    private LocalDateTime createdOn;
    private LocalDateTime finishedOn;
    private LocalDateTime startedOn;
    private LocalDateTime lastMoveOn;

    private List<Participant> participants = List.of();
    private Account owner;
    private Game game;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_ONGOING = 2;
    public static final int STATUS_FINISHED = 3;

    private Match() {
    }

    public Match(int matchId, int status, Integer nextAccountId, String data, int ownerId, int gameId, LocalDateTime createdOn, LocalDateTime finishedOn, LocalDateTime startedOn, LocalDateTime lastMoveOn) {
        this.matchId = matchId;
        this.status = status;
        this.nextAccountId = nextAccountId;
        this.data = data;
        this.gameId = gameId;
        this.ownerId = ownerId;
        this.createdOn = createdOn;
        this.finishedOn = finishedOn;
        this.startedOn = startedOn;
        this.lastMoveOn = lastMoveOn;
    }

    public int matchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int status() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Integer nextAccountId() { return nextAccountId; }
    public void setNextAccountId(Integer nextAccountId) { this.nextAccountId = nextAccountId; }

    public String data() { return data; }
    public void setData(String data) { this.data = data; }

    public int ownerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public int gameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public List<Participant> participants() { return participants; }
    public void setParticipants(List<Participant> participants) { this.participants = participants; }

    public Account owner() { return owner; }
    public void setOwner(Account owner) { this.owner = owner; }

    public Game game() { return game; }
    public void setGame(Game game) { this.game = game; }

    public LocalDateTime createdOn() { return createdOn; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }

    public LocalDateTime finishedOn() { return finishedOn; }
    public void setFinishedOn(LocalDateTime finishedOn) { this.finishedOn = finishedOn; }

    public LocalDateTime startedOn() { return startedOn; }
    public void setStartedOn(LocalDateTime startedOn) { this.startedOn = startedOn; }

    public LocalDateTime lastMoveOn() { return lastMoveOn; }
    public void setLastMoveOn(LocalDateTime lastMoveOn) { this.lastMoveOn = lastMoveOn; }

    @Override
    public String toString() {
        return "#" + matchId;
    }
}
