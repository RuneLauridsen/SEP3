package boardgames.shared.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static String databaseRebuildSql;

    static {
        databaseRebuildSql = ResourceUtil.readResourceAsString(DatabaseUtil.class, "database.sql");
    }

    public static void reset() {
        ConnectionPool pool = new ConnectionPool(1);
        try {
            Sql sql = new Sql(pool, databaseRebuildSql);
            sql.execute();
            sql.throwIfFailed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.close();
        }
    }
}
