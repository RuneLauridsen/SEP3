package boardgames.logic.games.tictactoe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class TicTacToeData {
    private int nextTurnAccountId;
    private TicTacToePlayer[] players;
    private char[] squares;
    private int moveCount;

    public TicTacToeData() {}

    public TicTacToeData(int nextTurnAccountId, TicTacToePlayer[] players, char[] squares) {
        this.nextTurnAccountId = nextTurnAccountId;
        this.players = players;
        this.squares = squares;
    }

    public int nextTurnAccountId() {
        return nextTurnAccountId;
    }

    public void setNextTurnAccountId(int nextTurnAccountId) {
        this.nextTurnAccountId = nextTurnAccountId;
    }

    public TicTacToePlayer[] players() {
        return players;
    }

    public void setPlayers(TicTacToePlayer[] players) {
        this.players = players;
    }

    public char[] squares() {
        return squares;
    }

    public void setSquares(char[] squares) {
        this.squares = squares;
    }

    public int moveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

}
