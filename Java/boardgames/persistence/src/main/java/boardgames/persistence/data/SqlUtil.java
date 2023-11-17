package boardgames.persistence.data;

import boardgames.shared.util.ResourceUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//
// TODO(rune): Hvis du en dag har lyst til at forbedre verdenssituationen og/eller
// livet generalt, ville det være rart med en Wrapper til PreparedStatement:
//
// - Gør det mere fail-safe at tilføje parametre.
// - Ikke brug for nogen seperator setNull()
// - Håndtere LocalDateTime mere pålideligt, clamp i stedet for overflow osv.
// - Ikke brug for setInt, setString osv(), bare brug overloads.
// - Overvej om vi kan bruge @Argument i stedet for '?' indices (hvis ikke det kan lade sig gøre,
//   så lav, så Wrapperen selv holder styr på en auto-increment index.)
//

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
