namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#
public class MoveResult {
    public required int Outcome { get; set; }
    public required int NextAccountId { get; set; }
    public required string NextData { get; set; }
    public required string InvalidMoveText { get; set; }
    public required string FinishedText { get; set; }
    public required Dictionary<int, int> Scores { get; set; }

    public const int OUTCOME_VALID = 1;      // NOTE(rune): Gyldigt træk -> forstæt til næste tur.
    public const int OUTCOME_INVALID = 2;    // NOTE(rune): Ugyldigt træk -> forsæt ikke til næste tur.
    public const int OUTCOME_FINISHED = 3;   // NOTE(rune): Match er færdig -> udfyld scores og update match status.
}
