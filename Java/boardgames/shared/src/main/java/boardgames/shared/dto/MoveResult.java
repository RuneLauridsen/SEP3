package boardgames.shared.dto;

import boardgames.shared.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;

// WARNING(rune): Navne skal matche mellem Java og C#
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class MoveResult {
    private final int outcome;
    private final String nextData;
    private final String invalidMoveText;
    private final String finishedText;
    private final Map<Integer, Integer> scores; // Key = accountId, Value = score.

    public final static int OUTCOME_VALID = 1;      // NOTE(rune): Gyldigt træk -> forstæt til næste tur.
    public final static int OUTCOME_INVALID = 2;    // NOTE(rune): Ugyldigt træk -> forsæt ikke til næste tur.
    public final static int OUTCOME_FINISHED = 3;   // NOTE(rune): Match er færdig -> udfyld scores og update match status.

    private MoveResult(int outcome, String nextData, String invalidMoveText, String finishedText) {
        this.outcome = outcome;
        this.nextData = nextData;
        this.invalidMoveText = invalidMoveText;
        this.finishedText = finishedText;
        this.scores = new HashMap<>();
    }

    public int outcome() { return outcome; }
    public String nextData() { return nextData; }
    public String invalidMoveText() { return invalidMoveText; }
    public String finishedText() { return finishedText; }
    public Map<Integer, Integer> scores() { return scores; }

    public static MoveResult invalid(String invalidMoveText) {
        return new MoveResult(OUTCOME_INVALID, "", invalidMoveText, "");
    }

    public static MoveResult finished(String nextState, String finishedText) {
        return new MoveResult(OUTCOME_FINISHED, nextState, "", finishedText);
    }

    public static MoveResult valid(String nextState) {
        return new MoveResult(OUTCOME_VALID, nextState, "", "");
    }

    public static MoveResult finished(Object nextState, String finishedText) {
        return finished(JsonUtil.toJson(nextState), finishedText);
    }

    public static MoveResult valid(Object nextState) {
        return valid(JsonUtil.toJson(nextState));
    }
}
