package boardgames.persistence.data;

import boardgames.shared.dto.Game;
import boardgames.shared.util.Timer;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.close;
import static boardgames.persistence.data.SqlUtil.openConnection;

@Service
public class GameDataSql implements GameData {
    private final Connection conn;

    public GameDataSql() {
        conn = openConnection();
    }

    private Game readGame(Sql sql) {
        return new Game(
            sql.readInt("game_id"),
            sql.readString("name"),
            "",
            ""
        );
    }

    private Game readGameWithPicture(Sql sql) {
        return new Game(
            sql.readInt("game_id"),
            sql.readString("name"),
            sql.readString("game_picture"),
            sql.readString("game_picture_type")
        );
    }

    @Override
    public Game get(int gameId) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.game WHERE game_id = ?");
        sql.set(gameId);
        return sql.querySingle(this::readGame);
    }

    @Override
    public Game getWithPicture(int gameId) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.game WHERE game_id = ?");
        sql.set(gameId);
        return sql.querySingle(this::readGameWithPicture);
    }

    @Override
    public List<Game> getAll() {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.game");
        return sql.queryAll(this::readGameWithPicture);
    }
}
