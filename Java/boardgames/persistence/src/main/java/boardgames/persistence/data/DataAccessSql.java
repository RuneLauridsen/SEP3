package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

/*
   TODO(rune): Meget gentaget kode -> factor til samlet funktion for:
    - Try-catch-finally blocks, med conn.prepareStatement() osv.
    - Mappe resultSet til DTO objekter, Account, Game osv.

   TODO(rune): Bonus point hvis vi splitter op i flere klasser, AcccountData, MatchData osv.
   (eller også skal vi bare bruge single-giant-class-in-a-single-giant-file-design-pattern™.
 */

@Service
public class DataAccessSql implements DataAccess {

    private final Connection conn;

    public DataAccessSql() {
        try {
            URI uri = getClass().getResource("/ConnectionString.txt").toURI();
            String connString = Files.readString(Path.of(uri));
            conn = DriverManager.getConnection(connString);
        } catch (SQLException | IOException | URISyntaxException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public Game getGame(int gameId) {
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
    public List<Game> getGames() {
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

    @Override
    public Account getAccount(int accountId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM account WHERE account_id = ?");
            stmt.setInt(1, accountId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getInt("account_status")
                );
                return account;
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
    public Account getAccount(String username) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getInt("account_status")
                );
                return account;
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
    public Account getAccount(String username, String hashedPassword) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? AND hashed_password = ?");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getInt("account_status")
                );
                return account;
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
    public Match createMatch(Account owner, Game game) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                INSERT INTO match
                    (match_id, state, owner_id, game_id)
                VALUES
                    (DEFAULT, '', ?, ?)
                RETURNING
                    match_id;
                """);

            stmt.setInt(1, owner.getAccountId());
            stmt.setInt(2, game.getGameId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                Match match = new Match(
                    rs.getInt("match_id"),
                    "",
                    owner.getAccountId(),
                    game.getGameId()
                );
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
    public int updateMatch(Match match) {
        PreparedStatement stmt = null;

        try {
            // NOTE(m2dx): Opdaterer kun state og owner, da det ikke mening
            // at opdatere game (brug createMatch() i stedet).
            stmt = conn.prepareStatement("""
                UPDATE match
                SET state = ?, owner_id = ?
                WHERE match_id = ?
                """);

            stmt.setString(1, match.getState());
            stmt.setInt(2, match.getOwnerId());
            stmt.setInt(3, match.getMatchId());
            int ret = stmt.executeUpdate();
            return ret;

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
        }
    }

    @Override
    public int deleteMatch(int matchId) {
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
    public Match getMatch(int matchId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                SELECT * FROM match WHERE match_id = ?
                """);

            stmt.setInt(1, matchId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Match match = new Match(
                    rs.getInt("match_id"),
                    rs.getString("state"),
                    rs.getInt("owner_id"),
                    rs.getInt("game_id")
                );
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
    public List<Match> getMatchesByAccount(Account account) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Match> matches = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("""
                SELECT DISTINCT m.*
                FROM match m
                LEFT OUTER JOIN participant p ON p.match_id = m.match_id
                WHERE m.owner_id = ? OR p.account_id = ? 
                """);

            stmt.setInt(1, account.getAccountId());
            stmt.setInt(2, account.getAccountId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                Match match = new Match(
                    rs.getInt("match_id"),
                    rs.getString("state"),
                    rs.getInt("owner_id"),
                    rs.getInt("game_id")
                );
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

    @Override
    public Participant createParticipant(Account account, Match match, int participantStatus) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                INSERT INTO participant
                    (participant_id, match_id, account_id, participant_status)
                VALUES
                    (DEFAULT, ?, ?, ?)
                RETURNING
                    participant_id
                """
            );

            stmt.setInt(1, match.getMatchId());
            stmt.setInt(2, account.getAccountId());
            stmt.setInt(3, participantStatus);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Participant participant = new Participant(
                    rs.getInt("participant_id"),
                    account,
                    participantStatus
                );
                return participant;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(stmt);
            close(rs);
        }

        return null;
    }

    @Override
    public List<Participant> getParticipants(Match match) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Participant> list = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("""
                SELECT p.*
                FROM participant p
                WHERE p.match_id = ?
                """);

            stmt.setInt(1, match.getMatchId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                Participant participant = new Participant(
                    rs.getInt("participant_id"),
                    getAccount(rs.getInt("account_id")),
                    rs.getInt("participant_status")
                );
                list.add(participant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }

        return list;
    }

    @Override
    public int updateParticipant(Participant participant) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // NOTE(m2dx): Opdaterer kun status, da det giver ikke mening
            // at opdatere match eller account (brug createParticipant() i stedet).
            stmt = conn.prepareStatement("""
                UPDATE participant
                SET participant_status = ?
                WHERE participant_id = ?
                """);

            stmt.setInt(1, participant.getParticipantStatus());
            stmt.setInt(2, participant.getParticipantId());
            int ret = stmt.executeUpdate();
            return ret;

        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }
    }

    @Override
    public int deleteParticipant(int participantId) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("DELETE FROM participant WHERE participant_id = ?");
            stmt.setInt(1, participantId);
            int ret = stmt.executeUpdate();
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
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
