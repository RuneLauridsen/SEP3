package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.close;
import static boardgames.persistence.data.SqlUtil.openConnection;

@Service
public class AccountDataSql implements AccountData {

    private final Connection conn;

    public AccountDataSql() {
        conn = openConnection();
    }

    @Override
    public Account get(int accountId) {
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
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getTimestamp("registration_datetime").toLocalDateTime(),
                    rs.getInt("status"),
                    rs.getTimestamp("created_on").toLocalDateTime()
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
    public Account get(String username) {
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
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getTimestamp("registration_datetime").toLocalDateTime(),
                    rs.getInt("status"),
                    rs.getTimestamp("created_on").toLocalDateTime()
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
    public Account get(String username, String hashedPassword) {
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
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getTimestamp("registration_datetime").toLocalDateTime(),
                    rs.getInt("status"),
                    rs.getTimestamp("created_on").toLocalDateTime()
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
    public List<Account> getAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Account> list = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM account ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getTimestamp("registration_datetime").toLocalDateTime(),
                    rs.getInt("status"),
                    rs.getTimestamp("created_on").toLocalDateTime()
                );
                list.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(rs);
            close(stmt);
        }

        return list;
    }
}
