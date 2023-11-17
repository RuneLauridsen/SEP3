package boardgames.persistence;

import java.util.List;

import boardgames.persistence.data.*;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunPersistence {
    public static void main(String[] args) {

        SpringApplication.run(RunPersistence.class, args);

        //
        // NOTE(rune): Poor man's unit test. Sikrer bare it vi ikke
        // får runtime fejl, men assert'er ikke resultater
        //

        // TODO(rune): Rigtige unit tests.
        GameData gameData = new GameDataSql();
        AccountData accountData = new AccountDataSql();
        MatchData matchData = new MatchDataSql();
        ParticipantData participantData = new ParticipantDataSql();

        Game game = gameData.get(2);

        Account account = accountData.get(2);
        account = accountData.get("Minii❤");

        Match match = matchData.create(account, game, "INITIAL_STATE");
        match = matchData.get(match.matchId());
        matchData.update(match);
        matchData.delete(matchData.create(account, game, "INITIAL_STATE").matchId());

        Participant participant = participantData.create(account, match, 0);
        List<Participant> participants = participantData.getAll(match.matchId(), -1, -1);
        participantData.update(participant);
        participantData.delete(participantData.create(account, match, 0).participantId());

        Account account1 = accountData.get("BenDover", "b025079c90813d4669136b2ed07512204ee05522ba3e647935f1a88daf00fd43");
    }
}
