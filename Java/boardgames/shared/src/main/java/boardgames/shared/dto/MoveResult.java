package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class MoveResult {
    private final String invalidMoveText; // NOTE(rune): Hvis invalidMoveText != "" -> ugyldigt træk.
    private final String winnerText; // NOTE(rune): Hvis winnerText != "" -> match er færdigt.

    private MoveResult(String invalidMoveText, String winnerText) {
        this.invalidMoveText = invalidMoveText;
        this.winnerText = winnerText;
    }

    public String invalidMoveText() {
        return invalidMoveText;
    }

    public String winnerText() {
        return winnerText;
    }

    public boolean isValid() {
        return invalidMoveText.isEmpty();
    }

    public boolean isInvalid() {
        return !invalidMoveText.isEmpty();
    }

    public boolean isWinning() {
        return !winnerText.isEmpty();
    }

    public static MoveResult invalid(String invalidMoveText) {
        return new MoveResult(invalidMoveText, "");
    }

    public static MoveResult winner(String winnerText) {
        return new MoveResult("", winnerText);
    }

    public static MoveResult valid() {
        return new MoveResult("", "");
    }
}
