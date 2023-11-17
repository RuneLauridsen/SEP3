package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
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
public class MatchDataSql implements MatchData {
    private final Connection conn;

    public MatchDataSql() {
        conn = openConnection();
    }

    @Override
    public Match create(Account owner, Game game, String data) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                INSERT INTO match
                    (match_id, status, data, owner_id, game_id, created_on)
                VALUES
                    (DEFAULT, 1, ?, ?, ?, DEFAULT)
                RETURNING
                    match_id, status, data, owner_id, game_id, created_on;
                """);

            stmt.setString(1, data);
            stmt.setInt(2, owner.accountId());
            stmt.setInt(3, game.gameId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                Match match = readMatch(rs);
                return match;
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
    public int update(Match match) {
        PreparedStatement stmt = null;

        try {
            // NOTE(m2dx): Opdaterer kun data og owner, da det ikke mening
            // at opdatere game (brug createMatch() i stedet).
            stmt = conn.prepareStatement("""
                UPDATE match
                SET data = ?, owner_id = ?
                WHERE match_id = ?
                """);

            stmt.setString(1, match.data());
            stmt.setInt(2, match.ownerId());
            stmt.setInt(3, match.matchId());
            int ret = stmt.executeUpdate();
            return ret;

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
        }
    }

    @Override
    public int delete(int matchId) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(" DELETE FROM match WHERE match_id = ? ");
            stmt.setInt(1, matchId);
            int ret = stmt.executeUpdate();
            return ret;

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
        }
    }

    @Override
    public Match get(int matchId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                SELECT * FROM match WHERE match_id = ?
                """);

            stmt.setInt(1, matchId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Match match = readMatch(rs);
                return match;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
            close(rs);
        }

        return null; // TODO(rune): Error handling.
    }

    @Override
    public List<Match> getAll(int accountId, int status) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Match> matches = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("""
                SELECT DISTINCT m.*
                FROM match m
                LEFT OUTER JOIN participant p ON p.match_id = m.match_id
                WHERE (? = -1 OR m.owner_id = ? OR p.account_id = ?)
                AND   (? = -1 OR m.status = ?)
                """);

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setInt(3, accountId);
            stmt.setInt(4, status);
            stmt.setInt(5, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Match match = readMatch(rs);
                matches.add(match);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
            close(rs);
        }

        return matches;
    }

    private static Match readMatch(ResultSet rs) throws SQLException {
        return new Match(
            rs.getInt("match_id"),
            rs.getInt("status"),
            rs.getString("data"),
            rs.getInt("owner_id"),
            rs.getInt("game_id"),
            rs.getTimestamp("created_on").toLocalDateTime()
        );
    }
}
