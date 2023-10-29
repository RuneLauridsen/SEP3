package boardgames.game.model;

import boardgames.game.services.AccountService;
import boardgames.game.services.GameService;
import boardgames.game.services.MatchService;
import boardgames.game.services.ParticipantService;
import boardgames.game.messages.Messages.*;
import boardgames.game.tictactoe.TicTacToe;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;

import java.util.List;

public class GameServerModelImpl implements GameServerModel {
    private final AccountService accountService;
    private final MatchService matchService;
    private final GameService gameService;
    private final ParticipantService participantService;

    public GameServerModelImpl(AccountService accountService, MatchService matchService, GameService gameService, ParticipantService participantService) {
        this.accountService = accountService;
        this.matchService = matchService;
        this.gameService = gameService;
        this.participantService = participantService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // TODO(rune): Username/password check.
        Account account = accountService.get(request.username());
        LoginResponse response = new LoginResponse(account);
        return response;
    }

    @Override
    public MoveResponse move(MoveRequest request, Account account) {
        Match match = matchService.get(request.matchId());

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
}
