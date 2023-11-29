package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.close;
import static boardgames.persistence.data.SqlUtil.openConnection;

@Service
public class MatchDataSql implements MatchData {
    private final Connection conn;

    public MatchDataSql() {
        conn = openConnection();
    }

    private Match readMatch(Sql sql) {
        return new Match(
            sql.readInt("match_id"),
            sql.readInt("status"),
            sql.readString("data"),
            sql.readInt("owner_id"),
            sql.readInt("game_id"),
            sql.readDateTime("created_on"),
            sql.readDateTime("finished_on")
        );
    }

    @Override
    public Match create(Account owner, Game game, String data) {
        Sql sql = new Sql(conn, """
            INSERT INTO boardgames.match
                (match_id, status, data, owner_id, game_id, created_on, finished_on)
            VALUES
                (DEFAULT, 1, ?, ?, ?, DEFAULT, DEFAULT)
            RETURNING
                match_id, status, data, owner_id, game_id, created_on, finished_on;
            """);

        sql.set(data);
        sql.set(owner.accountId());
        sql.set(game.gameId());
        return sql.querySingle(this::readMatch);
    }

    @Override
    public void update(Match match) {
        Sql sql = new Sql(conn, """
            UPDATE boardgames.match
            SET data = ?, owner_id = ?, status = ?, finished_on = ?
            WHERE match_id = ?
            """);

        sql.set(match.data());
        sql.set(match.ownerId());
        sql.set(match.status());
        sql.set(match.finishedOn());
        sql.set(match.matchId());
        sql.execute();
    }

    @Override
    public void delete(int matchId) {
        Sql sql = new Sql(conn, "DELETE FROM boardgames.match WHERE match_id = ? ");
        sql.set(matchId);
        sql.execute();
    }

    @Override
    public Match get(int matchId) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.match WHERE match_id = ?");
        sql.set(matchId);
        return sql.querySingle(this::readMatch);
    }

    @Override
    public List<Match> getAll(int accountId, int status) {
        Sql sql = new Sql(conn, """
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
