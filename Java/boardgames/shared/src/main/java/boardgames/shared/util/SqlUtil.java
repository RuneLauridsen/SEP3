package boardgames.shared.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {
    public static Connection openConnection() {
        try {
            String connString = ResourceUtil.readResourceAsString(SqlUtil.class, "/ConnectionString.txt");
            Connection conn = DriverManager.getConnection(connString);
            return conn;
        } catch (SQLException e) {
            Log.error(e);
            Log.error("Could not open connection to SQL database.");
            return null;
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.error(e);
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                Log.error(e);
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                Log.error(e);
            }
        }
    }
}
