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

public class RunLogicServer {
    public static void main(String[] args) {
        // TODO(rune): Hardcoded url til persistence server.
        String url = "http://localhost:8080";
        AccountService accountService = new AccountServiceRest(url);
        GameService gameService = new GameServiceRest(url);
        MatchService matchService = new MatchServiceRest(url);
        ParticipantService participantService = new ParticipantServiceRest(url);
        ScoreService scoreService = new ScoreServiceRest(url);
        JwtService jwtService = new JwtServiceAuth0();

        MessageQueue incomingQueue = new MessageQueue();
        MessageQueue outgoingQueue = new MessageQueue();

        LogicServerModel model = new LogicServerModelImpl(incomingQueue, outgoingQueue, accountService, matchService, gameService, participantService, scoreService, jwtService);
        LogicServer server = new LogicServerSocket(incomingQueue, outgoingQueue, 1234);

        Thread modelThread = new Thread(model);
        Thread serverThread = new Thread(server);

        modelThread.start();
        serverThread.start();

        try {
            modelThread.join();
            serverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
