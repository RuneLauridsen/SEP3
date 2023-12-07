package boardgames.shared.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static String databaseRebuildSql;

    static {
        databaseRebuildSql = ResourceUtil.readResourceAsString(DatabaseUtil.class, "database.sql");
    }

    public static void reset() {
        try {
            Connection conn = SqlUtil.openConnection();
            Sql sql = new Sql(conn, databaseRebuildSql);
            sql.execute();
            sql.throwIfFailed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
