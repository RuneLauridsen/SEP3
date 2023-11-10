package boardgames.game.model;

import boardgames.game.services.*;
import boardgames.game.messages.Messages.*;
import boardgames.game.tictactoe.TicTacToe;
import boardgames.shared.dto.*;

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

    //
    // Authorization
    //

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

    //
    // Matches
    //

    @Override
    public GetMatchesResponse getMatches(GetMatchesRequest getMatchesRequest) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(getMatchesRequest.jwt());
        List<Match> matches = List.of();

        if (claims.accountId() != 0) {
            matches = matchService.getByAccount(claims.accountId());
        }

        return new GetMatchesResponse(matches);
    }

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());

        CreateMatchParam param = new CreateMatchParam(claims.accountId(), req.gameId());
        Match match = matchService.create(param);
        return new CreateMatchResponse(match);
    }

    //
    // Spilkatalog
    //

    @Override
    public GetGamesResponse getGames(GetGamesRequest req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());
        List<Game> games = gameService.getGames();
        return new GetGamesResponse(games);
    }

    //
    // Participants
    //

    @Override
    public AddParticipantRes addParticipant(AddParticipantReq req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());
        Match match = matchService.get(req.matchId());
        if (match.getOwnerId() != claims.accountId()) {
            throw new NotAuthorizedException();
        }
        participantService.create(new CreateParticipantParam(req.accountId(), req.matchId()));
        return new AddParticipantRes("");
    }

    @Override
    public GetParticipantsRes getParticipants(GetParticipantsReq req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());
        List<Participant> participants = participantService.getByMatch(req.matchId());
        return new GetParticipantsRes(participants);
    }

    @Override
    public GetPendingRes getPending(GetPendingReq req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());
        List<Participant> pending = participantService.getByAccountAndStatus(claims.accountId(), Participant.PARTICIPANT_STATUS_PENDING);
        return new GetPendingRes(pending);
    }

    @Override
    public DecidePendingRes decidePending(DecidePendingReq req) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(req.jwt());
        Participant participant = participantService.get(req.participantId());
        if (participant.getAccountId() != claims.accountId()) {
            throw new NotAuthorizedException();
        }

        if (req.status() == Participant.PARTICIPANT_STATUS_ACCEPTED ||
            req.status() == Participant.PARTICIPANT_STATUS_REJECTED) {
            participant.setParticipantStatus(req.status());
            participantService.update(participant);
            return new DecidePendingRes("");
        } else {
            return new DecidePendingRes("Invalid participant status (was " + req.status() + " but must be ACCEPTED or REJECTED).");
        }
    }

    //
    // Move
    //

    @Override
    public MoveResponse move(MoveRequest request) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(request.jwt());
        Account account = accountService.get(claims.accountId());
        Match match = matchService.get(request.matchId());

        if (account == null) {
            return new MoveResponse(0, "", "No account found with id " + claims.accountId());
        }

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

}
