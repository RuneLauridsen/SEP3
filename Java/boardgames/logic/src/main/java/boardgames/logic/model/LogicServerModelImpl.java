package boardgames.logic.model;

import boardgames.logic.games.GameCatalog;
import boardgames.logic.games.GameLogic;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.Messages.*;
import boardgames.logic.services.*;
import boardgames.shared.dto.*;

import java.util.ArrayList;
import java.util.List;

public class LogicServerModelImpl implements LogicServerModel {
    private final AccountService accountService;
    private final MatchService matchService;
    private final GameService gameService;
    private final ParticipantService participantService;
    private final JwtService jwtService;

    public LogicServerModelImpl(AccountService accountService, MatchService matchService, GameService gameService, ParticipantService participantService, JwtService jwtService) {
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
    public LoginResponse login(LoginRequest req) {
        String hashedPassword = PasswordHashing.hash(req.password());
        Account account = accountService.get(req.username(), hashedPassword);
        if (account != null) {
            String jwt = jwtService.create(account);
            return new LoginResponse(true, account, jwt);
        } else {
            return new LoginResponse(false, Empty.account(), "");
        }
    }

    //
    // Matches
    //

    @Override
    public GetMyMatchesResponse getMyMatches(GetMyMatchesRequest req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        List<Match> matches = new ArrayList<>();

        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_PENDING));
        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_ONGOING));

        return new GetMyMatchesResponse(matches);
    }

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);

        // Create
        CreateMatchParam param = new CreateMatchParam(claims.accountId(), req.gameId());
        Match match = matchService.create(param);

        // Owner accepts automatically.
        AddParticipantReq addReq = new AddParticipantReq(match.matchId(), claims.accountId());
        AddParticipantRes addRes = addParticipant(addReq, jwt);
        DecidePendingReq decideReq = new DecidePendingReq(match.matchId(), addRes.participant().participantId(), Participant.STATUS_ACCEPTED);
        DecidePendingRes decideRes = decidePending(decideReq, jwt);

        return new CreateMatchResponse("", match);
    }

    //
    // Spilkatalog
    //

    @Override
    public GetGamesResponse getGames(GetGamesRequest req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        List<Game> games = gameService.getGames();
        return new GetGamesResponse(games);
    }

    @Override
    public GetMatchRes getMatch(GetMatchReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        Match match = matchService.get(req.matchId());
        if (match == null) {
            match = Empty.match();
        }
        return new GetMatchRes(match);
    }

    //
    // Participants
    //

    @Override
    public AddParticipantRes addParticipant(AddParticipantReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        Match match = matchService.get(req.matchId());
        if (match.ownerId() != claims.accountId()) {
            throw new NotAuthorizedException(String.format("Account id %d is not owner of match id %d (owner is account id %d).", claims.accountId(), match.matchId(), match.ownerId()));
        }
        Participant created = participantService.create(new CreateParticipantParam(req.accountId(), req.matchId()));
        return new AddParticipantRes(created, "");
    }

    @Override
    public GetParticipantsRes getParticipants(GetParticipantsReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        List<Participant> participants = participantService.getByMatch(req.matchId());
        return new GetParticipantsRes(participants);
    }

    @Override
    public GetPendingRes getPending(GetPendingReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        List<Participant> pending = participantService.getByAccountAndStatus(claims.accountId(), Participant.STATUS_PENDING);
        return new GetPendingRes(pending);
    }

    @Override
    public DecidePendingRes decidePending(DecidePendingReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);

        Match match = matchService.get(req.matchId());
        GameLogic gl = GameCatalog.get(match.gameId());
        GameSpec spec = gl.getSpec();

        Participant participant = Participants.getById(match.participants(), req.participantId());
        assert participant != null;         // TODO(rune): If participant == null then throw ...

        if (participant.accountId() != claims.accountId()) {
            throw new NotAuthorizedException();
        }

        if (req.status() == Participant.STATUS_ACCEPTED || req.status() == Participant.STATUS_REJECTED) {
            participant.setStatus(req.status());
            participantService.update(participant);

            // Begin match if no participants are pending.
            int pendingCount = Participants.countByStatus(match.participants(), Participant.STATUS_PENDING);
            int acceptedCount = Participants.countByStatus(match.participants(), Participant.STATUS_ACCEPTED);
            if (pendingCount == 0 && acceptedCount == spec.needPlayerCount()) {
                match.setData(gl.getInitialData(match));
                match.setStatus(Match.STATUS_ONGOING);
                matchService.update(match);
            }

            return new DecidePendingRes("");
        } else {
            return new DecidePendingRes("Invalid participant status (was " + req.status() + " but must be ACCEPTED or REJECTED).");
        }
    }

    //
    // Move
    //

    @Override
    public MoveRes move(MoveReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        Account account = accountService.get(claims.accountId());
        Match match = matchService.get(req.matchId());

        GameLogic gl = GameCatalog.get(match.gameId());
        MoveRes res = gl.validateMoveAndUpdateData(req, match, account);

        if (res.result().isWinning()) {
            match.setStatus(Match.STATUS_FINISHED);
        }

        if (res.result().isValid()) {
            matchService.update(match);
        }

        return res;
    }

    @Override
    public GetAccountsRes getAccounts(GetAccountsReq req, String jwt) throws NotAuthorizedException {
        JwtClaims claims = jwtService.verify(jwt);
        List<Account> accounts = accountService.get();
        return new GetAccountsRes(accounts);
    }

    @Override
    public UpdateUserStatusResponse approveUserReg(UpdateUserStatusRequest req, String jwt) throws NotAuthorizedException {
       JwtClaims claims = jwtService.verify(jwt);
       req.account().setStatus(req.newStatus());
       boolean success = accountService.updateStatus(req.account());
       return new UpdateUserStatusResponse(success);
    }

}
