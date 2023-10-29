package boardgames.shared.dto;

public class CreateMatchParam {
    private int gameId;
    private int ownerId;

    public CreateMatchParam() {
    }

    public CreateMatchParam(int ownerId, int gameId) {
        this.gameId = gameId;
        this.ownerId = ownerId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
