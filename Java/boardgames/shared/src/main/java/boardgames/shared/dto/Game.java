package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Game {
    private int gameId;
    private String name;

    public Game() {
    }

    public Game(int gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public int gameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
