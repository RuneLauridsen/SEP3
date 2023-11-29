package boardgames.persistence.data;

import boardgames.shared.dto.ScoreSum;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.openConnection;

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

    @Override
    public List<ScoreSum> getSums(int gameId) {
        // TODO(rune): FROM boardgames.game i stedet
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
}
