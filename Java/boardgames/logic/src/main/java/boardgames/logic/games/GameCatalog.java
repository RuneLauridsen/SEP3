package boardgames.logic.games;

import boardgames.logic.games.tictactoe.TicTacToe;

import java.util.HashMap;
import java.util.Map;

public class GameCatalog {
    private static final Map<String, GameLogic> map;

    static {
        map = new HashMap<>();
        put(new TicTacToe());
        // TODO(rune): put(new Stratego());
        // TODO(rune): put(...);
        // TODO(rune): put(...);
        // TODO(rune): put(...);
    }

    private static void put(GameLogic g) {
        GameCatalog.map.put(g.getSpec().identifier(), g);
    }

    public static GameLogic get(String identifier) {
        return map.get(identifier);
    }
}
