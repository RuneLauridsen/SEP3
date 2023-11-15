package boardgames.logic;

import boardgames.logic.model.GameServerModel;
import boardgames.logic.model.GameServerModelImpl;
import boardgames.logic.networking.GameServer;
import boardgames.logic.networking.GameServerSocket;
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
import boardgames.shared.dto.Account;
import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

import java.util.List;

public class RunLogicServer {
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
