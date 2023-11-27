package boardgames.logic.networking;

import boardgames.logic.model.LogicServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LogicServerSocket implements LogicServer {
    private final LogicServerModel model;
    private final int port;
    private ServerSocket serverSocket;
    private boolean quit;

    public LogicServerSocket(LogicServerModel model, int port) {
        this.model = model;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (!quit) {
                Socket socket = serverSocket.accept();
                LogicServerSocketHandler handler = new LogicServerSocketHandler(socket, model);

                Thread thread = new Thread(handler);
                thread.setDaemon(true);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        } finally {
            close(serverSocket);
        }
    }

    @Override
    public void close() {
        quit = true;
        close(serverSocket);
    }

    public static void close(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // NOTE(rune): Gør ikke noget hvis vi bare crasher her, da serveren
                // alligevel skal lukkes.
                throw new RuntimeException(e);
            }
        }
    }
}