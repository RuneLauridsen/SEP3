package boardgames.logic.model;

import boardgames.logic.games.GameCatalog;
import boardgames.logic.games.GameLogic;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.Messages.*;
import boardgames.logic.services.*;
import boardgames.shared.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogicServerModelImpl implements LogicServerModel {
    private final AccountService accountService;
    private final MatchService matchService;
    private final GameService gameService;
    private final ParticipantService participantService;
    private final ScoreService scoreService;
    private final JwtService jwtService;

    public LogicServerModelImpl(AccountService accountService, MatchService matchService, GameService gameService, ParticipantService participantService, ScoreService scoreService, JwtService jwtService) {
        this.accountService = accountService;
        this.matchService = matchService;
        this.gameService = gameService;
        this.participantService = participantService;
        this.scoreService = scoreService;
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
        Claims claims = jwtService.verify(jwt);
        List<Match> matches = new ArrayList<>();

        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_PENDING));
        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_ONGOING));

        return new GetMyMatchesResponse(matches);
    }

    @Override
    public CreateMatchResponse createMatch(CreateMatchRequest req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);

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
        Claims claims = jwtService.verify(jwt);
        List<Game> games = gameService.getGames();
        return new GetGamesResponse(games);
    }

    @Override
    public GetMatchRes getMatch(GetMatchReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
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
        Claims claims = jwtService.verify(jwt);
        Match match = matchService.get(req.matchId());

        // Tjek korrekt account.
        if (match.ownerId() != claims.accountId()) {
            throw new NotAuthorizedException(String.format("Account id %d is not owner of match id %d (owner is account id %d).", claims.accountId(), match.matchId(), match.ownerId()));
        }

        // Tjek antal invites.
        List<Participant> ps = match.participants();
        GameSpec spec = GameCatalog.getSpec(match.gameId());
        int needCount = spec.needPlayerCount();
        int acceptedCount = Participants.countByStatus(ps, Participant.STATUS_ACCEPTED);
        int pendingCount = Participants.countByStatus(ps, Participant.STATUS_PENDING);
        int totalCount = acceptedCount + pendingCount;
        if (totalCount >= needCount) {
            String reason = String.format("Cannot add another participant. Currently %d players are invited, and game needs %d to start.", totalCount, needCount);
            return new AddParticipantRes(Empty.participant(), reason);
        }

        // Alt ok -> opret i persistence.
        CreateParticipantParam param = new CreateParticipantParam(req.accountId(), req.matchId());
        Participant p = participantService.create(param);
        return new AddParticipantRes(p, "");
    }

    @Override
    public GetParticipantsRes getParticipants(GetParticipantsReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Participant> participants = participantService.getByMatch(req.matchId());
        return new GetParticipantsRes(participants);
    }

    @Override
    public GetPendingRes getPending(GetPendingReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Participant> pending = participantService.getByAccountAndStatus(claims.accountId(), Participant.STATUS_PENDING);
        return new GetPendingRes(pending);
    }

    @Override
    public DecidePendingRes decidePending(DecidePendingReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);

        Match match = matchService.get(req.matchId());
        GameLogic gl = GameCatalog.getLogic(match.gameId());
        GameSpec spec = gl.spec();

        Participant participant = Participants.getById(match.participants(), req.participantId());
        assert participant != null;         // TODO(rune): If participant == null then throw ...

        if (participant.accountId() != claims.accountId()) {
            throw new NotAuthorizedException();
        }

        boolean validStatus = req.status() == Participant.STATUS_ACCEPTED ||
                              req.status() == Participant.STATUS_REJECTED;

        if (validStatus) {
            participant.setStatus(req.status());
            participantService.update(participant);

            // Begin match if no participants are pending.
            int pendingCount = Participants.countByStatus(match.participants(), Participant.STATUS_PENDING);
            int acceptedCount = Participants.countByStatus(match.participants(), Participant.STATUS_ACCEPTED);
            if (pendingCount == 0 && acceptedCount == spec.needPlayerCount()) {
                var players = Participants.withoutStatus(match.participants(), Participant.STATUS_REJECTED);
                var data = gl.getInitialData(players);
                match.setData(data);
                match.setStatus(Match.STATUS_ONGOING);
                match.setStartedOn(LocalDateTime.now());
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
        Claims claims = jwtService.verify(jwt);
        Account account = accountService.get(claims.accountId());
        Match match = matchService.get(req.matchId());

        GameLogic gl = GameCatalog.getLogic(match.gameId());
        MoveResult result = gl.validateMoveAndUpdateData(req, match, account);

        switch (result.outcome()) {
            case MoveResult.OUTCOME_VALID -> {
                match.setData(result.nextData());
                matchService.update(match);

            }

            case MoveResult.OUTCOME_INVALID -> {
                // Ingenting.
            }

            case MoveResult.OUTCOME_FINISHED -> {
                match.setStatus(Match.STATUS_FINISHED);
                match.setFinishedOn(LocalDateTime.now());
                match.setData(result.nextData());
                matchService.update(match);
                for (Participant p : match.participants()) {
                    p.setStatus(Participant.STATUS_FINISHED);
                    p.setScore(result.scores().getOrDefault(p.accountId(), 0));
                    participantService.update(p);
                }
            }
        }

        MoveRes response = new MoveRes(match.matchId(), match.data(), result);
        return response;
    }
    @Override
    public GetAccountRes getAccount(GetAccountReq req, String jwt) throws NotAuthorizedException {
        jwtService.verify(jwt);
        Account account = accountService.get(req.accountId());
        GetAccountRes res = new GetAccountRes(account);
        return res;
    }

    @Override
    public GetAccountsRes getAccounts(GetAccountsReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Account> allAccounts = accountService.get();
        List<Account> returnAccounts = allAccounts;

        // NOTE(rune): Hvis ikke admin -> vis kun accounts som er accepted.
        if (!claims.isAdmin()) {
            returnAccounts = new ArrayList<>();
            for (Account a : allAccounts) {
                if (a.status() == Account.STATUS_ACCEPTED) {
                    returnAccounts.add(a);
                }
            }
        }

        return new GetAccountsRes(returnAccounts);
    }

    @Override
    public UpdateUserStatusResponse approveUserReg(UpdateUserStatusRequest req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        req.account().setStatus(req.newStatus());
        boolean success = accountService.update(req.account());
        return new UpdateUserStatusResponse(success);
    }
    @Override
    public UpdateAccountRes updateAccount(UpdateAccountReq req, String jwt) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        Account fromClient = req.account();
        Account fromServer = accountService.get(fromClient.accountId());

        // TODO(rune): Username already taken.
        // TODO(rune): Følgende tjek gælder ikke for admin.
        Validation v = new Validation();

        if (fromClient.accountId() != claims.accountId()) {
            v.invalid(String.format("Tried to edit account id %s but jwt account id was %s.", fromClient.accountId(), claims.accountId()));
        }

        v.equal(fromClient, fromServer, Account::registerDateTime, "register date time");
        v.equal(fromClient, fromServer, Account::createdOn, "created on");
        v.equal(fromClient, fromServer, Account::status, "status");

        v.mustBeNonEmpty(fromClient, Account::username, "username");
        v.mustBeNonEmpty(fromClient, Account::firstName, "first name");
        v.mustBeNonEmpty(fromClient, Account::lastName, "last name");
        v.mustBeNonEmpty(fromClient, Account::email, "email");

        v.mustBeShortedThan(fromClient, Account::description, "description", 500);

        if (v.isValid()) {
            accountService.update(fromClient);
            return new UpdateAccountRes("");
        } else {
            return new UpdateAccountRes(v.reason());
        }
    }
    @Override
    public GetScoreSumsResponse getScoreSums(GetScoreSumsRequest req, String jwt) throws NotAuthorizedException {
        jwtService.verify(jwt);
        List<ScoreSum> sums = scoreService.getSums(req.gameId());
        GetScoreSumsResponse res = new GetScoreSumsResponse(sums);
        return res;
    }
    @Override
    public GetMatchHistoryResponse getMatchHistory(GetMatchHistoryRequest req, String jwt) throws NotAuthorizedException {
        jwtService.verify(jwt);
        List<MatchScore> scores = scoreService.getScores(req.accountId());
        GetMatchHistoryResponse res = new GetMatchHistoryResponse(scores);
        return res;
    }
}
