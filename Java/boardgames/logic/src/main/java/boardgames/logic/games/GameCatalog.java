package boardgames.logic.games;

import boardgames.logic.games.tictactoe.TicTacToeLogic;

import java.util.HashMap;
import java.util.Map;

public class GameCatalog {
    private static final Map<Integer, TurnBasedGameLogic> map; // NOTE(rune): Key = samme id som i database.

    static {
        map = new HashMap<>();
        map.put(1, new TicTacToeLogic());
    }

    public static TurnBasedGameLogic getLogic(Integer gameId) {
        return map.get(gameId);
    }

    public static GameSpec getSpec(Integer gameId) {
        return map.get(gameId).spec();
    }
}
