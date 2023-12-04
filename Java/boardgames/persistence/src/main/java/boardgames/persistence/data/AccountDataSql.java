package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

import static boardgames.persistence.data.SqlUtil.close;
import static boardgames.persistence.data.SqlUtil.openConnection;

@Service
public class AccountDataSql implements AccountData {

    private final Connection conn;

    public AccountDataSql() {
        conn = openConnection();
    }

    private Account readAccount(Sql sql) {
        return new Account(
            sql.readInt("account_id"),
            sql.readString("username"),
            sql.readString("first_name"),
            sql.readString("last_name"),
            sql.readString("email"),
            sql.readString("description"),
            sql.readDateTime("registration_datetime"),
            sql.readInt("status"),
            sql.readDateTime("created_on"),
            sql.readBoolean("is_admin")
        );
    }

    private Account readAccountWithProfilePicture(Sql sql) {
        Account ret = readAccount(sql);
        ret.setProfilePicture(sql.readString("profile_picture"));
        ret.setProfilePictureType(sql.readString("profile_picture_type"));
        return ret;
    }

    @Override
    public Account getWithPicture(int accountId) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.account WHERE account_id = ?");
        sql.set(accountId);
        return sql.querySingle(this::readAccountWithProfilePicture);
    }

    @Override
    public Account get(int accountId) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.account WHERE account_id = ?");
        sql.set(accountId);
        return sql.querySingle(this::readAccount);
    }

    @Override
    public Account get(String username) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.account WHERE username = ?");
        sql.set(username);
        return sql.querySingle(this::readAccountWithProfilePicture);
    }

    @Override
    public Account get(String username, String hashedPassword) {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.account WHERE username = ? AND hashed_password = ?");
        sql.set(username);
        sql.set(hashedPassword);
        return sql.querySingle(this::readAccountWithProfilePicture);
    }

    @Override
    public List<Account> getAll() {
        Sql sql = new Sql(conn, "SELECT * FROM boardgames.account WHERE status <> 3 -- STATUS_DELETED");
        return sql.queryAll(this::readAccount);
    }

    @Override
    public void update(Account account) {
        Sql sql = new Sql(conn, """
            UPDATE boardgames.account
            SET username = ?,
                first_name = ?,
                last_name = ?,
                email = ?,
                description = ?,
                registration_datetime = ?,
                status = ?
            WHERE account_id = ?
            """);

        sql.set(account.username());
        sql.set(account.firstName());
        sql.set(account.lastName());
        sql.set(account.email());
        sql.set(account.description());
        sql.set(account.registerDateTime());
        sql.set(account.status());
        sql.set(account.accountId());
        sql.execute();

        if (account.profilePicture() != null &&
            account.profilePictureType() != null) {
            sql = new Sql(conn, """
                UPDATE boardgames.account
                SET profile_picture = ?,
                    profile_picture_type = ?
                WHERE account_id = ?
                """);

            sql.set(account.profilePicture());
            sql.set(account.profilePictureType());
            sql.set(account.accountId());
            sql.execute();
        }
    }
}
