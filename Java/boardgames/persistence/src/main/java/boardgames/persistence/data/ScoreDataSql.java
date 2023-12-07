package boardgames.persistence.data;

import boardgames.shared.dto.FinishedMatchScore;
import boardgames.shared.dto.ScoreSum;
import boardgames.shared.util.Sql;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.shared.util.SqlUtil.openConnection;

@Service
public class ScoreDataSql implements ScoreData {
    private final Connection conn;

    public ScoreDataSql() {
        conn = openConnection();
    }

    private ScoreSum readScoreSum(Sql sql) {
        return new ScoreSum(
            sql.readInt("game_id"),
            sql.readInt("account_id"),
            sql.readString("account_name"),
            sql.readInt("score"),
            sql.readInt("count")
        );
    }

    private FinishedMatchScore readMatchScore(Sql sql) {
        return new FinishedMatchScore(
            sql.readInt("game_id"),
            sql.readString("game_name"),
            sql.readInt("match_id"),
            sql.readDateTime("match_finished_on"),
            sql.readInt("score")
        );
    }

    @Override
    public List<ScoreSum> getSums(int gameId) {
        Sql sql = new Sql(conn, """
            SELECT g.game_id,
                   a.account_id,
                   a.username AS account_name,
                   SUM(p.score) AS score,
                   COUNT(*) AS count
            FROM boardgames.account a
            INNER JOIN boardgames.participant p
                INNER JOIN boardgames.match m
                    INNER JOIN boardgames.game g
                    ON g.game_id = m.game_id
                    AND g.game_id = ?
                ON m.match_id = p.match_id
            ON p.account_id = a.account_id
            AND p.status = 4 -- STATUS_FINISHED
            GROUP BY a.account_id, g.game_id
            ORDER BY SUM(p.score) DESC""");

        sql.set(gameId);
        List<ScoreSum> sums = sql.queryAll(this::readScoreSum);
        return sums;

    }
    @Override
    public List<FinishedMatchScore> getScores(int accountId) {
        Sql sql = new Sql(conn, """
            SELECT g.game_id        AS game_id,
                   g.name           AS game_name,
                   m.match_id       AS match_id,
                   m.finished_on    AS match_finished_on,
                   p.score          AS score
            FROM boardgames.account a
            INNER JOIN boardgames.participant p
                INNER JOIN boardgames.match m
                    INNER JOIN boardgames.game g
                    ON g.game_id = m.game_id
                ON m.match_id = p.match_id
            ON p.account_id = a.account_id
            AND p.status = 4 -- STATUS_FINISHED
            WHERE a.account_id = ?
            ORDER BY m.finished_on DESC
            """);

        sql.set(accountId);
        List<FinishedMatchScore> ret = sql.queryAll(this::readMatchScore);
        return ret;
    }
}
