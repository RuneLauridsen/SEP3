package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.ANY,
    setterVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class Game {
    private int gameId;
    private String name;
    private String gamePicture;     // NOTE(rune): Som base 64
    private String gamePictureType; // NOTE(rune): MIME content type.

    private Game() {
    }

    public Game(int gameId, String name, String gamePicture, String gamePictureType) {
        this.gameId = gameId;
        this.name = name;
        this.gamePicture = gamePicture;
        this.gamePictureType = gamePictureType;
    }

    public int gameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public String name() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGamePicture() { return gamePicture; }
    public void setGamePicture(String gamePicture) { this.gamePicture = gamePicture; }

    public String getGamePictureType() { return gamePictureType; }
    public void setGamePictureType(String gamePictureType) { this.gamePictureType = gamePictureType; }
}
