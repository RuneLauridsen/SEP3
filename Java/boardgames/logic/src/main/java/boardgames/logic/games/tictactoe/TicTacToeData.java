package boardgames.logic.games.tictactoe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.ANY,
    setterVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class TicTacToeData {
    public int piecesO;
    public int piecesX;
    public int accountIdO;
    public int accountIdX;
    public int remainingO;
    public int remainingX;
    public int moveCount;
}
