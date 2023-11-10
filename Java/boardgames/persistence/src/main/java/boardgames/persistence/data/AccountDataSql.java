package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
