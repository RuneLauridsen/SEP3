package boardgames.logic.networking;

import boardgames.logic.messages.MessageQueue;
import boardgames.logic.messages.QueuedMessage;
import boardgames.shared.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogicServerSocket implements LogicServer {
    private final MessageQueue incomingQueue;
    private final MessageQueue outgoingQueue;
    private final Thread distributorThread;
    private final Thread acceptorThread;
    private final int port;

    private ServerSocket serverSocket;
    private boolean quit;
    private int clientIdentCounter = 1;

    private final ConcurrentMap<Integer, LogicSocket> currentClients;

    public LogicServerSocket(MessageQueue incomingQueue, MessageQueue outgoingQueue, int port) {
        this.port = port;
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = outgoingQueue;

        this.distributorThread = new Thread(this::distributorThreadProc);
        this.acceptorThread = new Thread(this::acceptorThreadProc);
        this.currentClients = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        distributorThread.start();
        acceptorThread.start();

        try {
            distributorThread.join();
            acceptorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acceptorThreadProc() {
        try {
            serverSocket = new ServerSocket(port);

            while (!quit) {
                Socket socket = serverSocket.accept();

                int clientIdent = clientIdentCounter++;
                LogicSocket client = new LogicSocket(socket, clientIdent, incomingQueue);

                Thread readerThread = new Thread(() -> {
                    client.readerThreadProc();
                    currentClients.remove(client.clientIdent());
                });

                Thread writerThread = new Thread(() -> {
                    client.writerThreadProc();
                    currentClients.remove(client.clientIdent());
                });

                readerThread.setDaemon(true);
                writerThread.setDaemon(true);
                readerThread.start();
                writerThread.start();

                currentClients.put(clientIdent, client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(serverSocket);
        }
    }

    public void distributorThreadProc() {
        while (!quit) {
            QueuedMessage fromQueue = outgoingQueue.pull(10_000);
            Log.info("Outgoing " + fromQueue.message().head().bodyType() + " to clientIdent " + fromQueue.clientIdent());
            if (fromQueue != null) {
                LogicSocket client = currentClients.get(fromQueue.clientIdent());
                if (client != null) {
                    client.outgoingQueue().post(fromQueue);
                }
            }
        }
    }

    public void shutdown() {
        quit = true;
        distributorThread.interrupt();
        acceptorThread.interrupt();
        close(serverSocket);
    }

    private static void close(ServerSocket serverSocket) {
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
