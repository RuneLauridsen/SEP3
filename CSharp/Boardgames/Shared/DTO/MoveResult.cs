namespace GameClient.DTO;

// WARNING(rune): Navne skal matche mellem Java og C#
public class MoveResult {
    public required String InvalidMoveText { get; set; } // NOTE(rune): Hvis invalidMoveText != "" -> ugyldigt træk.
    public required String WinnerText { get; set; } // NOTE(rune): Hvis winnerText != "" -> match er færdigt.

    public bool IsValid() {
        return InvalidMoveText == "";
    }

    public bool IsInvalid() {
        return InvalidMoveText != "";
    }

    public bool IsWinning() {
        return WinnerText != "";
    }
}
