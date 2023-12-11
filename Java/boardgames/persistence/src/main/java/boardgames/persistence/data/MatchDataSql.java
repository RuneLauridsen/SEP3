package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.util.ConnectionPool;
import boardgames.shared.util.Sql;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.shared.util.SqlUtil.close;
import static boardgames.shared.util.SqlUtil.openConnection;

@Service
public class MatchDataSql implements MatchData {
    private final ConnectionPool pool;

    public MatchDataSql() {
        pool = new ConnectionPool(5);
    }

    private Match readMatch(Sql sql) {
        return new Match(
            sql.readInt("match_id"),
            sql.readInt("status"),
            sql.readInteger("next_account_id"),
            sql.readString("data"),
            sql.readInt("owner_id"),
            sql.readInt("game_id"),
            sql.readDateTime("created_on"),
            sql.readDateTime("finished_on"),
            sql.readDateTime("started_on"),
            sql.readDateTime("last_move_on")
        );
    }

    @Override
    public Match create(Account owner, Game game, String data) {
        Sql sql = new Sql(pool, """
            INSERT INTO boardgames.match
                (match_id, status, data, owner_id, game_id, created_on, finished_on, started_on, last_move_on, next_account_id)
            VALUES
                (DEFAULT, 1, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)
            RETURNING
                match_id, status, data, owner_id, game_id, created_on, finished_on, started_on, last_move_on, next_account_id;
            """);

        sql.set(data);
        sql.set(owner.accountId());
        sql.set(game.gameId());
        return sql.querySingle(this::readMatch);
    }

    @Override
    public void update(Match match) {
        Sql sql = new Sql(pool, """
            UPDATE boardgames.match
            SET data = ?,
                next_account_id = ?,
                owner_id = ?,
                status = ?, 
                finished_on = ?, 
                started_on = ?, 
                last_move_on = ?
            WHERE match_id = ?
            """);

        sql.set(match.data());
        sql.set(match.nextAccountId());
        sql.set(match.ownerId());
        sql.set(match.status());
        sql.set(match.finishedOn());
        sql.set(match.startedOn());
        sql.set(match.lastMoveOn());
        sql.set(match.matchId());
        sql.execute();
    }

    @Override
    public void delete(int matchId) {
        Sql sql = new Sql(pool, "DELETE FROM boardgames.match WHERE match_id = ? ");
        sql.set(matchId);
        sql.execute();
    }

    @Override
    public Match get(int matchId) {
        Sql sql = new Sql(pool, "SELECT * FROM boardgames.match WHERE match_id = ?");
        sql.set(matchId);
        return sql.querySingle(this::readMatch);
    }

    @Override
    public List<Match> getAll(int accountId, int status) {
        Sql sql = new Sql(pool, """
            SELECT DISTINCT m.*
            FROM boardgames.match m
            LEFT OUTER JOIN boardgames.participant p ON p.match_id = m.match_id
            WHERE (? = -1 OR m.owner_id = ? OR p.account_id = ?)
            AND   (? = -1 OR m.status = ?)
            """);

        sql.set(accountId);
        sql.set(accountId);
        sql.set(accountId);
        sql.set(status);
        sql.set(status);
        return sql.queryAll(this::readMatch);
    }
}
