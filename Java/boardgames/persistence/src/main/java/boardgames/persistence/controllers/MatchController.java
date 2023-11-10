package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.persistence.data.GameData;
import boardgames.persistence.data.MatchData;
import boardgames.shared.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class MatchController {
    private final MatchData matchData;
    private final AccountData accountData;
    private final GameData gameData;

    public MatchController(MatchData matchData, AccountData accountData, GameData gameData) {
        this.matchData = matchData;
        this.accountData = accountData;
        this.gameData = gameData;
    }

    @PostMapping("matches")
    public Match create(@RequestBody CreateMatchParam param) {
        int ownerId = param.getOwnerId();
        int gameId = param.getGameId();

        Account owner = accountData.get(ownerId);
        Game game = gameData.get(gameId);

        throwIfNotFound(ownerId, owner);
        throwIfNotFound(gameId, game);

        Match match = matchData.create(owner, game);
        return match;
    }

    @GetMapping("matches/{matchId}")
    public Match get(@PathVariable int matchId) {
        Match match = matchData.get(matchId);

        throwIfNotFound(matchId, match);

        return match;
    }

    @PutMapping("matches/{matchId}")
    public void update(@PathVariable int matchId, @RequestBody Match match) {
        throwIfMismatched(matchId, match);
        matchData.update(match);
    }

    @DeleteMapping("matches/{matchId}")
    public void delete(@PathVariable int matchId) {
        matchData.delete(matchId);
    }

    @GetMapping("matches")
    public List<Match> getByAccount(int accountId) {
        Account account = accountData.get(accountId);
        throwIfNotFound(accountId, account);
        List<Match> matches = matchData.getByAccount(account);
        return matches;
    }
}
