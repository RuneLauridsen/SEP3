package boardgames.game;

import boardgames.game.model.GameServerModel;
import boardgames.game.model.GameServerModelImpl;
import boardgames.game.networking.GameServer;
import boardgames.game.networking.GameServerSocket;
import boardgames.game.services.*;
import boardgames.shared.dto.*;

import java.util.List;

public class RunGameServer {
    public static void main(String[] args) {
        // TODO(rune): Hardcoded url til persistence server.
        String ulr = "http://localhost:8080";
        AccountService accountService = new AccountServiceRest(ulr);
        GameService gamePersistence = new GameServiceRest(ulr);
        MatchService matchService = new MatchServiceRest(ulr);
        ParticipantService participantService = new ParticipantServiceRest(ulr);
        JwtService jwtService = new JwtServiceAuth0();

        //
        // NOTE(rune): Poor man's unit test.
        //

        if (false) {

            // TODO(rune): Rigtige unit tests.

            Game game = new Game(1, "TicTacToe");

            Account account = accountService.get(2);
            account = accountService.get("Minii❤");

            Match match = matchService.create(new CreateMatchParam(account.getAccountId(), game.getGameId()));
            match = matchService.get(match.getMatchId());
            matchService.update(match);
            matchService.delete(matchService.create(new CreateMatchParam(account.getAccountId(), game.getGameId())).getMatchId());

            Participant participant = participantService.create(new CreateParticipantParam(account.getAccountId(), match.getMatchId(), true));
            List<Participant> participants = participantService.get(match.getMatchId());
            participantService.update(participant);
            participantService.delete(participantService.create(new CreateParticipantParam(account.getAccountId(), match.getMatchId(), true)).getParticipantId());
        }

        //
        // NOTE(rune): Åbn socket
        //

        GameServerModel model = new GameServerModelImpl(accountService, matchService, gamePersistence, participantService, jwtService);
        GameServer server = new GameServerSocket(model, 1234);
        server.run();
    }
}
