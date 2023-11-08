package boardgames.persistence.controllers;

import boardgames.persistence.data.DataAccess;
import boardgames.persistence.exceptions.BadRequestException;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.throwIfMismatched;
import static boardgames.persistence.controllers.ControllerUtil.throwIfNotFound;

@RestController
public class ParticipantController {
    private final DataAccess dataAccess;

    public ParticipantController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @GetMapping("matches/{matchId}/participants")
    public List<Participant> get(@PathVariable int matchId) {
        Match match = dataAccess.getMatch(matchId);

        throwIfNotFound(matchId, match);

        List<Participant> participants = dataAccess.getParticipants(match);
        return participants;
    }

    @PostMapping("matches/{matchId}/participants")
    public Participant create(@PathVariable int matchId, @RequestBody CreateParticipantParam param) {
        if (matchId != param.getMatchId()) {
            throw new BadRequestException("Match id in path (" + matchId + ") does not match id in body (" + matchId + ").");
        }

        int accountId = param.getAccountId();
        Match match = dataAccess.getMatch(matchId);
        Account account = dataAccess.getAccount(param.getAccountId());

        throwIfNotFound(matchId, match);
        throwIfNotFound(accountId, account);

        Participant participant = dataAccess.createParticipant(account, match, 0);
        return participant;
    }

    @PutMapping("participants/{participantId}")
    public void update(@PathVariable int participantId, @RequestBody Participant participant) {
        throwIfMismatched(participantId, participant);
        dataAccess.updateParticipant(participant);
    }

    @DeleteMapping("participants/{participantId}")
    public void delete(@PathVariable int participantId) {
        dataAccess.deleteParticipant(participantId);
    }
}
