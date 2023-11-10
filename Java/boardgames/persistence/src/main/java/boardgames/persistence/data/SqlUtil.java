package boardgames.persistence.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

/*
   TODO(rune): Meget gentaget kode i alle XyzDataSql -> factor til samlet funktion for:
    - Try-catch-finally blocks, med conn.prepareStatement() osv.
    - Mappe resultSet til DTO objekter, Account, Game osv.
 */

public class SqlUtil {
    public static Connection openConnection() {
        try {
            URI uri = SqlUtil.class.getResource("/ConnectionString.txt").toURI();
            String connString = Files.readString(Path.of(uri));
            Connection conn = DriverManager.getConnection(connString);
            return conn;
        } catch (SQLException | IOException | URISyntaxException e) {
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