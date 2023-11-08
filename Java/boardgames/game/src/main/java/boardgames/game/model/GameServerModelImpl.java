package boardgames.game.model;

import boardgames.game.services.*;
import boardgames.game.messages.Messages.*;
import boardgames.game.tictactoe.TicTacToe;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;

import java.util.List;

public class GameServerModelImpl implements GameServerModel {
    private final AccountService accountService;
    private final MatchService matchService;
    private final GameService gameService;
    private final ParticipantService participantService;
    private final JwtService jwtService;

    public GameServerModelImpl(AccountService accountService, MatchService matchService, GameService gameService, ParticipantService participantService, JwtService jwtService) {
        this.accountService = accountService;
        this.matchService = matchService;
        this.gameService = gameService;
        this.participantService = participantService;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String hashedPassword = PasswordHashing.hash(request.password());
        Account account = accountService.get(request.username(), hashedPassword);
        if (account != null) {
            String jwt = jwtService.create(account);
            return new LoginResponse(true, account, jwt);
        } else {
            return new LoginResponse(false, Account.empty(), "");
        }
    }

    @Override
    public MoveResponse move(MoveRequest request, Account account) {
        Match match = matchService.get(request.matchId());

        // TODO(rune): JWT
        String jwt = "";
        if (match == null) {
            return new MoveResponse(0, "", "No match found with id " + request.matchId());
        }

        switch (match.getGameId()) {
            case TicTacToe.TAC_TAC_TOE_GAME_ID -> {
                String check = TicTacToe.checkMove(account, match.getState(), request.gameState());
                if (check.isEmpty()) {
                    match.setState(request.gameState());
                    matchService.update(match);
                }
                return new MoveResponse(match.getMatchId(), match.getState(), check);
            }

            default -> {
                return new MoveResponse(match.getMatchId(), "", "Unknown game id " + match.getMatchId());
            }
        }
    }

    @Override
    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest, Account account) {
        List<Match> matches = List.of();

        if (account != null) {
            matches = matchService.getByAccount(account.getAccountId());
        }

        return new GetMatchesResponse(matches);
    }

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest req) {
        JwtClaims claims = jwtService.verify(req.jwt());

        CreateMatchParam param = new CreateMatchParam(claims.accountId, req.gameId());
        Match match = matchService.create(param);
        return new CreateMatchResponse(match);
    }

    @Override
    public GetGamesResponse getGames(GetGamesRequest req) {
        JwtClaims claims = jwtService.verify(req.jwt());
        List<Game> games = gameService.getGames();
        return new GetGamesResponse(games);
    }
}
