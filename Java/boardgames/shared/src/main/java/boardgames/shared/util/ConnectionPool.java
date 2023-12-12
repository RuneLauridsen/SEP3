package boardgames.shared.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionPool {
    private final ArrayList<Connection> freeConnections;
    private final ArrayList<Connection> busyConnections;

    public ConnectionPool() {
        freeConnections = new ArrayList<>();
        busyConnections = new ArrayList<>();
    }

    public ConnectionPool(int connectionCount) {
        this();
        addConnections(connectionCount);
    }

    public synchronized void addConnections(int connectionCount) {
        for (int i = 0; i < connectionCount; i++) {
            Connection c = newConnection();
            if (c != null) {
                freeConnections.add(newConnection());
            } else {
                // Kunne ikke åbne forbindelse, what do?
                // Fejl er allerede logget i SqlUtil.
            }
        }
    }

    public synchronized Connection acquireConnection() {
        while (freeConnections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Log.error(e);
            }
        }

        Connection connection = freeConnections.remove(0);
        busyConnections.add(connection);

        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        busyConnections.remove(connection);
        freeConnections.add(connection);
        notifyAll();
    }

    public synchronized void close() {
        while (!busyConnections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Log.error(e);
            }
        }

        for (Connection connection : freeConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.error(e);
            }
        }

        freeConnections.clear();
    }

    private static Connection newConnection() {
        Connection c = SqlUtil.openConnection();
        return c;
    }
}
