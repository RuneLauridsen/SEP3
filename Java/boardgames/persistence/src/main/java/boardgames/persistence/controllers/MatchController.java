package boardgames.persistence.controllers;

import boardgames.persistence.data.DataAccess;
import boardgames.shared.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class MatchController {
    private final DataAccess dataAccess;

    public MatchController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @PostMapping("matches")
    public Match create(@RequestBody CreateMatchParam param) {
        int ownerId = param.getOwnerId();
        int gameId = param.getGameId();

        Account owner = dataAccess.getAccount(ownerId);
        Game game = dataAccess.getGame(gameId);

        throwIfNotFound(ownerId, owner);
        throwIfNotFound(gameId, game);

        Match match = dataAccess.createMatch(owner, game);
        return match;
    }

    @GetMapping("matches/{matchId}")
    public Match get(@PathVariable int matchId) {
        Match match = dataAccess.getMatch(matchId);

        throwIfNotFound(matchId, match);

        return match;
    }

    @PutMapping("matches/{matchId}")
    public void update(@PathVariable int matchId, @RequestBody Match match) {
        throwIfMismatched(matchId, match);
        dataAccess.updateMatch(match);
    }

    @DeleteMapping("matches/{matchId}")
    public void delete(@PathVariable int matchId) {
        dataAccess.deleteParticipant(matchId);
    }

    @GetMapping("matches")
    public List<Match> getByAccount(int accountId) {
        Account account = dataAccess.getAccount(accountId);
        throwIfNotFound(accountId, account);
        List<Match> matches = dataAccess.getMatchesByAccount(account);
        return matches;
    }
}
