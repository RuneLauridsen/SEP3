namespace GameClient.Games;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class TicTacToeData {
    public required int PiecesO { get; set; }
    public required int PiecesX { get; set; }
    public required int AccountIdO { get; set; }
    public required int AccountIdX { get; set; }
    public required int RemainingO { get; set; }
    public required int RemainingX { get; set; }
    public required int MoveCount { get; set; }
}

// WARNING(rune): Navne skal matche mellem Java og C#.
public class TicTacToeMove {
    public required int PlaceOnIndex { get; set; }
    public required int TakeFromIndex { get; set; }
}
