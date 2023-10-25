package boardgames.persistence;

import boardgames.shared.Account;
import boardgames.shared.Game;
import boardgames.shared.Session;

import java.util.ArrayList;
import java.util.List;

import java.awt.image.BandCombineOp;
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

            if (rs.next()) {
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

    public void addAccountToSession(Session session, Account account) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("""
                    INSERT INTO session_participant 
                        (session_id, account_id)
                    VALUES 
                        (?, ?);
                    """
            );

            stmt.setInt(1, session.getSessionId());
            stmt.setInt(2, account.getAccountId());
            stmt.execute();

        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        } finally {
            close(stmt);
        }
    }

    public List<Account> getAccountsInSession(Session session) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Account> list = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("""
                    SELECT a.*
                    FROM session_participant sp
                    INNER JOIN account a on sp.account_id = a.account_id
                    WHERE sp.session_id = ?
                    """);

            stmt.setInt(1, session.getSessionId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                Account acc = new Account();
                acc.setAccountId(rs.getInt("account_id"));
                acc.setUsername(rs.getString("username"));
                list.add(acc);
            }

        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(stmt);
        }

        return list;
    }

    public void updateSessionState(Session session, String newState) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("""
                    UPDATE session SET state = ? WHERE session_id = ?
                    """
            );

            stmt.setString(1, newState);
            stmt.setInt(2, session.getSessionId());
            stmt.execute();

        } catch (SQLException e) {
            // TODO(rune): Error handling.
            throw new RuntimeException(e);
        } finally {
            close(stmt);
        }
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
