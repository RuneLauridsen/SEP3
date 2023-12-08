package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.persistence.data.GameData;
import boardgames.persistence.data.MatchData;
import boardgames.persistence.data.ParticipantData;
import boardgames.shared.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class MatchController {
    private final MatchData matchData;
    private final AccountData accountData;
    private final GameData gameData;
    private final ParticipantData participantData;

    public MatchController(MatchData matchData, AccountData accountData, GameData gameData, ParticipantData participantData) {
        this.matchData = matchData;
        this.accountData = accountData;
        this.gameData = gameData;
        this.participantData = participantData;
    }

    private void includeParticipants(Match m) {
        List<Participant> participants = participantData.getAll(m.matchId(), -1, -1);
        m.setParticipants(participants);
        for (Participant p : participants) {
            Account a = accountData.get(p.accountId());
            p.setAccount(a);
        }
    }

    private void includeGame(Match match) {
        Game game = gameData.get(match.gameId());
        match.setGame(game);
    }

    @PostMapping("matches")
    public Match create(@RequestBody CreateMatchParam param) {
        int ownerId = param.ownerId();
        int gameId = param.gameId();

        Account owner = accountData.get(ownerId);
        Game game = gameData.get(gameId);

        throwIfNotFound(ownerId, owner);
        throwIfNotFound(gameId, game);

        Match match = matchData.create(owner, game, "");
        return match;
    }

    @GetMapping("matches/{matchId}")
    public Match get(@PathVariable int matchId) {
        Match match = matchData.get(matchId);
        throwIfNotFound(matchId, match);

        includeParticipants(match);
        includeGame(match);

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
    public List<Match> getAll(@RequestParam(defaultValue = "-1") int accountId,
                              @RequestParam(defaultValue = "-1") int status) {
        List<Match> matches = matchData.getAll(accountId, status);

        for (Match m : matches) {
            includeGame(m);
            includeParticipants(m);
        }

        return matches;
    }
}
