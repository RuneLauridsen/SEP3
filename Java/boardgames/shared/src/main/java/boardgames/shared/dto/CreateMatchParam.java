package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.ANY,
    setterVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateMatchParam {
    private int gameId;
    private int ownerId;

    private CreateMatchParam() {
    }

    public CreateMatchParam(int ownerId, int gameId) {
        this.gameId = gameId;
        this.ownerId = ownerId;
    }

    public int gameId() {
        return gameId;
    }

    public int ownerId() {
        return ownerId;
    }
}
