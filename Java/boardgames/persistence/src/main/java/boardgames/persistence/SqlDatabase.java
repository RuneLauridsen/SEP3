package boardgames.persistence;

import boardgames.shared.Account;
import boardgames.shared.Game;
import boardgames.shared.Session;

import java.sql.*;

public class SqlDatabase {

    private Connection conn;

    public SqlDatabase() {
        try {
            // TODO(rune): Hard-coded connection string.
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=asdasd&currentSchema=boardgames");
        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        }
    }

    public Account getAccount(String username) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Account acc = new Account();
                acc.setAccountId(rs.getInt("account_id"));
                acc.setUsername(rs.getString("username"));
                return acc;
            }
        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(stmt);
        }

        return null;
    }

    public Session createSession(Game game) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(
            """
                INSERT INTO session 
                    (session_id, state, game_id) 
                VALUES 
                    (DEFAULT, '', ?)
                RETURNING 
                    session_id;
                """);

            stmt.setInt(1, game.getGameId());
            rs = stmt.executeQuery();

            if(rs.next()) {
                Session session = new Session();
                session.setSessionId(rs.getInt("session_id"));
                session.setGame(game);
                session.setState("");
                return session;
            }

        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(stmt);
        }

        return null;
    }

    private static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
