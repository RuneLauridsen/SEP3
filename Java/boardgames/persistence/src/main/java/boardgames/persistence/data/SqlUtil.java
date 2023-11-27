package boardgames.persistence.data;

import boardgames.shared.util.ResourceUtil;

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
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
