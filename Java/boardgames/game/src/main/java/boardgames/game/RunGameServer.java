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
        GameService gameService = new GameServiceRest(ulr);
        MatchService matchService = new MatchServiceRest(ulr);
        ParticipantService participantService = new ParticipantServiceRest(ulr);
        JwtService jwtService = new JwtServiceAuth0();

        //
        // NOTE(rune): Poor man's unit test.
        //

        if (true) {

            // TODO(rune): Rigtige unit tests.

            Game game = new Game(1, "TicTacToe");

            Account account = accountService.get(2);
            account = accountService.get("Minii❤");

            Match match = matchService.create(new CreateMatchParam(account.accountId(), game.gameId()));
            match = matchService.get(match.matchId());
            matchService.update(match);
            matchService.delete(matchService.create(new CreateMatchParam(account.accountId(), game.gameId())).matchId());

            Participant participant = participantService.create(new CreateParticipantParam(account.accountId(), match.matchId()));
            List<Participant> participants = participantService.getByMatch(match.matchId());
            participantService.update(participant);
            participantService.delete(participantService.create(new CreateParticipantParam(account.accountId(), match.matchId())).participantId());

            Account account1 = accountService.get("BenDover", "b025079c90813d4669136b2ed07512204ee05522ba3e647935f1a88daf00fd43");

            List<Game> games1 = gameService.getGames();
        }

        //
        // NOTE(rune): Åbn socket
        //

        GameServerModel model = new GameServerModelImpl(accountService, matchService, gameService, participantService, jwtService);
        GameServer server = new GameServerSocket(model, 1234);
        server.run();
    }
}
