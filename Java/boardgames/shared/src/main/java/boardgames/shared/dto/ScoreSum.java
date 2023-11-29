package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class ScoreSum {
    private int gameId;
    private int accountId;
    private String accountName;
    private int score;
    private int count;

    public ScoreSum() {
    }

    public ScoreSum(int gameId, int accountId, String accountName, int score, int count) {
        this.gameId = gameId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.score = score;
        this.count = count;
    }

    public int gameId() { return gameId; }
    public String accountName() { return accountName; }
    public int accountId() { return accountId; }
    public int score() { return score; }
    public int count() { return count; }
}
