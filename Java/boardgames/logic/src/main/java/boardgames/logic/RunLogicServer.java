package boardgames.logic;

import boardgames.logic.games.GameCatalog;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.MessageQueue;
import boardgames.logic.model.LogicServerModel;
import boardgames.logic.model.LogicServerModelImpl;
import boardgames.logic.networking.LogicServer;
import boardgames.logic.networking.LogicServerSocket;
import boardgames.logic.services.AccountService;
import boardgames.logic.services.AccountServiceRest;
import boardgames.logic.services.GameService;
import boardgames.logic.services.GameServiceRest;
import boardgames.logic.services.JwtService;
import boardgames.logic.services.JwtServiceAuth0;
import boardgames.logic.services.MatchService;
import boardgames.logic.services.MatchServiceRest;
import boardgames.logic.services.ParticipantService;
import boardgames.logic.services.ParticipantServiceRest;
import boardgames.logic.services.ScoreService;
import boardgames.logic.services.ScoreServiceRest;
import boardgames.shared.util.Log;

public class RunLogicServer {
    private LogicServerModel model;
    private LogicServerSocket server;

    private Thread modelThread;
    private Thread serverThread;

    public static void main(String[] args) {
        RunLogicServer app = new RunLogicServer();
        app.run(args);
    }

    public void run() {
        run(new String[0]);
    }

    public void run(String[] args) {
        String persistenceUrl = "http://localhost:8080";
        if (args.length == 0) {
            System.out.println("First argument is not persistence server url, using default '" + persistenceUrl + "' instead.");
        } else {
            persistenceUrl = args[0];
        }

        AccountService accountService = new AccountServiceRest(persistenceUrl);
        GameService gameService = new GameServiceRest(persistenceUrl);
        MatchService matchService = new MatchServiceRest(persistenceUrl);
        ParticipantService participantService = new ParticipantServiceRest(persistenceUrl);
        ScoreService scoreService = new ScoreServiceRest(persistenceUrl);
        JwtService jwtService = new JwtServiceAuth0();

        MessageQueue incomingQueue = new MessageQueue();
        MessageQueue outgoingQueue = new MessageQueue();

        model = new LogicServerModelImpl(incomingQueue, outgoingQueue, accountService, matchService, gameService, participantService, scoreService, jwtService);
        server = new LogicServerSocket(incomingQueue, outgoingQueue, 1234);

        modelThread = new Thread(model);
        serverThread = new Thread(server);

        modelThread.start();
        serverThread.start();

        try {
            modelThread.join();
            serverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        modelThread.interrupt();
        serverThread.interrupt();

        try {
            model.shutdown();
        } catch (Exception e) {
            Log.error(e);
        }

        try {
            server.shutdown();
        } catch (Exception e) {
            Log.error(e);
        }
    }
}
