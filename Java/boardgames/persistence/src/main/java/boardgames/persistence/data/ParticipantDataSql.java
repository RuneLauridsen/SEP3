package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import boardgames.shared.util.ConnectionPool;
import boardgames.shared.util.Sql;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.shared.util.SqlUtil.close;
import static boardgames.shared.util.SqlUtil.openConnection;

@Service
public class ParticipantDataSql implements ParticipantData {
    private final ConnectionPool pool;

    public ParticipantDataSql() {
        pool = new ConnectionPool(5);
    }

    private Participant readParticipant(Sql sql) {
        return new Participant(
            sql.readInt("participant_id"),
            sql.readInt("status"),
            sql.readInt("match_id"),
            sql.readInt("account_id"),
            sql.readDateTime("created_on"),
            sql.readInt("score")
        );
    }

    @Override
    public Participant create(Account account, Match match, int status) {
        Sql sql = new Sql(pool, """
            INSERT INTO boardgames.participant
                (participant_id, match_id, account_id, status, created_on, score)
            VALUES
                (DEFAULT, ?, ?, ?, DEFAULT, DEFAULT)
            RETURNING
                participant_id, match_id, account_id, status, created_on, score
            """);

        sql.set(match.matchId());
        sql.set(account.accountId());
        sql.set(status);
        return sql.querySingle(this::readParticipant);
    }

    @Override
    public Participant get(int participantId) {
        Sql sql = new Sql(pool, """
            SELECT p.*
            FROM boardgames.participant p
            WHERE p.participant_id = ?
            """);

        sql.set(participantId);
        return sql.querySingle(this::readParticipant);
    }

    @Override
    public List<Participant> getAll(int matchId, int accountId, int status) {
        Sql sql = new Sql(pool, """
            SELECT p.*
            FROM boardgames.participant p
            WHERE (? = -1 OR p.match_id = ?)
            AND   (? = -1 OR p.account_id = ?)
            AND   (? = -1 OR p.status = ?)
            """);

        sql.set(matchId);
        sql.set(matchId);
        sql.set(accountId);
        sql.set(accountId);
        sql.set(status);
        sql.set(status);
        return sql.queryAll(this::readParticipant);
    }

    @Override
    public void update(Participant participant) {
        // NOTE(m2dx): Opdaterer kun status og score, da det giver ikke mening
        // at opdatere match eller account (brug createParticipant() i stedet).
        Sql sql = new Sql(pool, """
            UPDATE boardgames.participant
            SET status = ?,
                score = ?
            WHERE participant_id = ?
            """);

        sql.set(participant.status());
        sql.set(participant.score());
        sql.set(participant.participantId());
        sql.execute();
    }

    @Override
    public void delete(int participantId) {
        Sql sql = new Sql(pool, "DELETE FROM boardgames.participant WHERE participant_id = ?");
        sql.set(participantId);
        sql.execute();
    }
}
