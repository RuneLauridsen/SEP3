package boardgames.persistence.data;

import boardgames.shared.dto.Game;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.close;
import static boardgames.persistence.data.SqlUtil.openConnection;

@Service
public class GameDataSql implements GameData {
    private final Connection conn;

    public GameDataSql() {
        conn = openConnection();
    }

    @Override
    public Game get(int gameId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM game WHERE game_id = ?");
            stmt.setInt(1, gameId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Game game = new Game(
                    rs.getInt("game_id"),
                    rs.getString("name")
                );
                return game;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }

        return null; // TODO(rune): Error handling
    }

    @Override
    public List<Game> getAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Game> ret = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM game");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Game game = new Game(
                    rs.getInt("game_id"),
                    rs.getString("name")
                );
                ret.add(game);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }

        return ret;
    }

}
