package boardgames.shared.dto;

import boardgames.shared.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;

// WARNING(rune): Navne skal matche mellem Java og C#
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.ANY,
    setterVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class MoveResult {
    private final int outcome;
    private final String nextData;
    private final String invalidMoveText;
    private final String finishedText;
    private final Map<Integer, Integer> scores; // Key = accountId, Value = score.
    private final int nextAccountId;

    public final static int OUTCOME_VALID = 1;      // NOTE(rune): Gyldigt træk -> forstæt til næste tur.
    public final static int OUTCOME_INVALID = 2;    // NOTE(rune): Ugyldigt træk -> forsæt ikke til næste tur.
    public final static int OUTCOME_FINISHED = 3;   // NOTE(rune): Match er færdig -> udfyld scores og update match status.

    private MoveResult(int outcome, int nextAccountId, String nextData, String invalidMoveText, String finishedText) {
        this.outcome = outcome;
        this.nextAccountId = nextAccountId;
        this.nextData = nextData;
        this.invalidMoveText = invalidMoveText;
        this.finishedText = finishedText;
        this.scores = new HashMap<>();
    }

    public int outcome() { return outcome; }
    public String nextData() { return nextData; }
    public int nextAccountId() { return nextAccountId; }
    public String invalidMoveText() { return invalidMoveText; }
    public String finishedText() { return finishedText; }
    public Map<Integer, Integer> scores() { return scores; }

    public static MoveResult invalid(String invalidMoveText) {
        return new MoveResult(OUTCOME_INVALID, 0, "", invalidMoveText, "");
    }

    public static MoveResult finished(String nextState, String finishedText) {
        return new MoveResult(OUTCOME_FINISHED, 0, nextState, "", finishedText);
    }

    public static MoveResult valid(int nextAccountId, String nextState) {
        return new MoveResult(OUTCOME_VALID, nextAccountId, nextState, "", "");
    }

    public static MoveResult finished(Object nextState, String finishedText) {
        return finished(JsonUtil.toJson(nextState), finishedText);
    }

    public static MoveResult valid(int nextAccountId, Object nextState) {
        return valid(nextAccountId, JsonUtil.toJson(nextState));
    }
}
