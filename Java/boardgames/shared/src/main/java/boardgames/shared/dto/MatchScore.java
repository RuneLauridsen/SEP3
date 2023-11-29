package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

// TODO(rune): Bedre navn.

// NOTE(rune):
// MatchScore = en række som bliver returneret når man beder om en brugers
// match historik. Account er implicit, fordi alle MatchScore records vil
// være for samme bruger som account id fra requesten.

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class MatchScore {
    private int gameId;
    private String gameName;
    private int matchId;
    private LocalDateTime matchFinishedOn;
    private int score;

    public MatchScore() {
    }

    public MatchScore(int gameId, String gameName, int matchId, LocalDateTime matchFinishedOn, int score) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.matchId = matchId;
        this.matchFinishedOn = matchFinishedOn;
        this.score = score;
    }

    public int gameId() { return gameId; }
    public String gameName() { return gameName; }
    public int matchId() { return matchId; }
    public LocalDateTime matchFinishedOn() { return matchFinishedOn; }
    public int score() { return score; }
}
