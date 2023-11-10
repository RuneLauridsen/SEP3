package boardgames.persistence.controllers;

import boardgames.persistence.data.GameData;
import boardgames.shared.dto.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {
    private final GameData gameData;

    public GameController(GameData gameData) {
        this.gameData = gameData;
    }

    @GetMapping("games")
    public List<Game> get() {
        List<Game> game = gameData.getAll();
        return game;
    }
}
