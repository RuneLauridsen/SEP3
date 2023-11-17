namespace GameClient.Games;

// WARNING(rune): Navne skal matche mellem Java og C#.
public class TicTacToePlayer {
    public required int AccountId { get; set; }
    public required char Team { get; set; } // TODO(rune): Bedre navn? Team == om man spiller som kryds eller bolle. Husk også at ændr i Java.
    public required int RemainingPieces { get; set; }
}

// WARNING(rune): Navne skal matche mellem Java og C#.
public class TicTacToeData {
    public required int NextTurnAccountId { get; set; }
    public required TicTacToePlayer[] Players { get; set; }
    public required char[] Squares { get; set; }
    public required int MoveCount { get; set; }
}

// WARNING(rune): Navne skal matche mellem Java og C#.
public class TicTacToeMove {
    public required int PlaceOnIndex { get; set; }
    public required int TakeFromIndex { get; set; }
}
