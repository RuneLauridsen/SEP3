package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
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
public class ParticipantDataSql implements ParticipantData {
    private final Connection conn;

    public ParticipantDataSql() {
        conn = openConnection();
    }

    @Override
    public Participant create(Account account, Match match, int status) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                INSERT INTO participant
                    (participant_id, match_id, account_id, status, created_on)
                VALUES
                    (DEFAULT, ?, ?, ?, DEFAULT)
                RETURNING
                    participant_id, match_id, account_id, status, created_on
                """
            );

            stmt.setInt(1, match.matchId());
            stmt.setInt(2, account.accountId());
            stmt.setInt(3, status);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Participant participant = new Participant(
                    rs.getInt("participant_id"),
                    rs.getInt("status"),
                    rs.getInt("match_id"),
                    rs.getInt("account_id"),
                    rs.getTimestamp("created_on").toLocalDateTime()
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
    public Participant get(int participantId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("""
                SELECT p.*
                FROM participant p
                WHERE p.participant_id = ?
                """);

            stmt.setInt(1, participantId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Participant(
                    rs.getInt("participant_id"),
                    rs.getInt("status"),
                    rs.getInt("match_id"),
                    rs.getInt("account_id"),
                    rs.getTimestamp("created_on").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }

        return null;
    }

    @Override
    public List<Participant> getAll(int matchId, int accountId, int status) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Participant> list = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("""
                SELECT p.*
                FROM participant p
                WHERE (? = -1 OR p.match_id = ?)
                AND   (? = -1 OR p.account_id = ?)
                AND   (? = -1 OR p.status = ?)
                """);

            stmt.setInt(1, matchId);
            stmt.setInt(2, matchId);
            stmt.setInt(3, accountId);
            stmt.setInt(4, accountId);
            stmt.setInt(5, status);
            stmt.setInt(6, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Participant participant = new Participant(
                    rs.getInt("participant_id"),
                    rs.getInt("status"),
                    rs.getInt("match_id"),
                    rs.getInt("account_id"),
                    rs.getTimestamp("created_on").toLocalDateTime()
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
    public int update(Participant participant) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // NOTE(m2dx): Opdaterer kun status, da det giver ikke mening
            // at opdatere match eller account (brug createParticipant() i stedet).
            stmt = conn.prepareStatement("""
                UPDATE participant
                SET status = ?
                WHERE participant_id = ?
                """);

            stmt.setInt(1, participant.status());
            stmt.setInt(2, participant.participantId());
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
    public int delete(int participantId) {
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
}
