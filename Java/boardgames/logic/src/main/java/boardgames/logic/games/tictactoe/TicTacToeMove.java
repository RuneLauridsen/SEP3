package boardgames.logic.games.tictactoe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche med C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class TicTacToeMove {
    public int placeOnIndex;
    public int takeFromIndex;

    public TicTacToeMove() {}

    public TicTacToeMove(int placeOnIndex, int takeFromIndex) {
        this.placeOnIndex = placeOnIndex;
        this.takeFromIndex = takeFromIndex;
    }

    public int placeOnIndex() {
        return placeOnIndex;
    }

    public void setPlaceOnIndex(int placeOnIndex) {
        this.placeOnIndex = placeOnIndex;
    }

    public int takeFromIndex() {
        return takeFromIndex;
    }

    public void setTakeFromIndex(int takeFromIndex) {
        this.takeFromIndex = takeFromIndex;
    }
}
