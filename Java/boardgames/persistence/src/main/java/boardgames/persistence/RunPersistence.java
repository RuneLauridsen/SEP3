package boardgames.persistence;

import java.util.List;

import boardgames.persistence.data.DataAccessSql;
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
        DataAccessSql db = new DataAccessSql();
        Game game = db.getGame(2);

        Account account = db.getAccount(2);
        account = db.getAccount("Minii❤");

        Match match = db.createMatch(account, game);
        match = db.getMatch(match.getMatchId());
        db.updateMatch(match);
        db.deleteMatch(db.createMatch(account, game).getMatchId());

        Participant participant = db.createParticipant(account, match, 0);
        List<Participant> participants = db.getParticipants(match);
        db.updateParticipant(participant);
        db.deleteParticipant(db.createParticipant(account, match, 0).getParticipantId());

        Account account1 = db.getAccount("BenDover", "b025079c90813d4669136b2ed07512204ee05522ba3e647935f1a88daf00fd43");
    }
}
