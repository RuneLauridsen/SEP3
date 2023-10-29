package boardgames.shared.dto;

public class Match {
    private int matchId;
    private String state;
    private int ownerId;
    private int gameId;

    public Match() {
    }

    public Match(int matchId, String state, int ownerId, int gameId) {
        this.matchId = matchId;
        this.state = state;
        this.gameId = gameId;
        this.ownerId = ownerId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "#" + matchId;
    }
}
