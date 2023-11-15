package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.persistence.data.GameData;
import boardgames.persistence.data.MatchData;
import boardgames.persistence.data.ParticipantData;
import boardgames.persistence.exceptions.BadRequestException;
import boardgames.shared.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.throwIfMismatched;
import static boardgames.persistence.controllers.ControllerUtil.throwIfNotFound;

@RestController
public class ParticipantController {
    private final ParticipantData participantData;
    private final MatchData matchData;
    private final AccountData accountData;
    private final GameData gameData;

    public ParticipantController(ParticipantData participantData, MatchData matchData, AccountData accountData, GameData gameData) {
        this.participantData = participantData;
        this.matchData = matchData;
        this.accountData = accountData;
        this.gameData = gameData;
    }

    @GetMapping("participants/{participantId}")
    public Participant get(@PathVariable int participantId) {
        Participant participant = participantData.get(participantId);
        throwIfNotFound(participantId, participant);
        return participant;
    }

    @GetMapping("participants")
    public List<Participant> get(@RequestParam(defaultValue = "-1") int matchId,
                                 @RequestParam(defaultValue = "-1") int accountId,
                                 @RequestParam(defaultValue = "-1") int participantStatus) {
        if ((matchId != -1) ||
            (accountId != -1) ||
            (participantStatus != -1)) {
            List<Participant> participants = participantData.getAll(matchId, accountId, participantStatus);
            for (Participant participant : participants) {
                Account account = accountData.get(participant.accountId());
                Match match = matchData.get(participant.matchId());
                Account owner = accountData.get(match.ownerId());
                Game game = gameData.get(match.gameId());
                match.setGame(game);
                match.setOwner(owner);
                participant.setAccount(account);
                participant.setMatch(match);
            }

            return participants;

        } else {
            return List.of();
        }
    }

    @PostMapping("matches/{matchId}/participants")
    public Participant create(@PathVariable int matchId, @RequestBody CreateParticipantParam param) {
        if (matchId != param.matchId()) {
            throw new BadRequestException("Match id in path (" + matchId + ") does not match id in body (" + matchId + ").");
        }

        int accountId = param.accountId();
        Match match = matchData.get(matchId);
        Account account = accountData.get(param.accountId());

        throwIfNotFound(matchId, match);
        throwIfNotFound(accountId, account);

        Participant participant = participantData.create(account, match, Participant.STATUS_PENDING);
        return participant;
    }

    @PutMapping("participants/{participantId}")
    public void update(@PathVariable int participantId, @RequestBody Participant participant) {
        throwIfMismatched(participantId, participant);
        participantData.update(participant);
    }

    @DeleteMapping("participants/{participantId}")
    public void delete(@PathVariable int participantId) {
        participantData.delete(participantId);
    }
}
