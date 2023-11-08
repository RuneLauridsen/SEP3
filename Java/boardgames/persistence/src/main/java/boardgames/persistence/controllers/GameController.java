package boardgames.persistence.controllers;

import boardgames.persistence.data.DataAccess;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class GameController {
    private final DataAccess dataAccess;

    public GameController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @GetMapping("games")
    public List<Game> get() {
        List<Game> game = dataAccess.getGames();
        return game;
    }
}
