package boardgames.logic.games.tictactoe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class TicTacToePlayer {
    private int accountId;
    private char team; // TODO(rune): Bedre navn? Team == om man spiller som kryds eller bolle. Husk også at ændr i Java.
    private int remainingPieces;

    public TicTacToePlayer() {}

    public TicTacToePlayer(int accountId, char team, int remainingPieces) {
        this.accountId = accountId;
        this.team = team;
        this.remainingPieces = remainingPieces;
    }

    public int accountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public char team() {
        return team;
    }

    public void setTeam(char team) {
        this.team = team;
    }

    public int remainingPieces() {
        return remainingPieces;
    }

    public void setRemainingPieces(int remainingPieces) {
        this.remainingPieces = remainingPieces;
    }
}
