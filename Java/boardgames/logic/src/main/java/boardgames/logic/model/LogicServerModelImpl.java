package boardgames.logic.model;

import boardgames.logic.games.GameCatalog;
import boardgames.logic.games.TurnBasedGameLogic;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.QueuedMessage;
import boardgames.logic.messages.MessageQueue;
import boardgames.logic.messages.Message;
import boardgames.logic.messages.Messages.*;
import boardgames.logic.messages.MessageHandler;
import boardgames.logic.messages.MessageHandlers;
import boardgames.logic.services.*;
import boardgames.shared.dto.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LogicServerModelImpl implements LogicServerModel, Runnable {
    private final AccountService accountService;
    private final MatchService matchService;
    private final GameService gameService;
    private final ParticipantService participantService;
    private final ScoreService scoreService;
    private final JwtService jwtService;

    private final MessageQueue incomingQueue;
    private final MessageQueue outgoingQueue;
    private final IndexedLocks matchMutexes;
    private final MessageHandlers handlers;

    // NOTE(rune): Key = accountId, value = clientIdent.
    // Viser hvilke clientIdent'er der lytter til en accountId.
    private final ConcurrentMultiValueHashMap<Integer, Integer> listeners;

    public LogicServerModelImpl(
        MessageQueue incomingQueue,
        MessageQueue outgoingQueue,
        AccountService accountService,
        MatchService matchService,
        GameService gameService,
        ParticipantService participantService,
        ScoreService scoreService,
        JwtService jwtService) {

        this.incomingQueue = incomingQueue;
        this.outgoingQueue = outgoingQueue;

        this.accountService = accountService;
        this.matchService = matchService;
        this.gameService = gameService;
        this.participantService = participantService;
        this.scoreService = scoreService;
        this.jwtService = jwtService;

        this.matchMutexes = new IndexedLocks();
        this.listeners = new ConcurrentMultiValueHashMap<>();

        this.handlers = new MessageHandlers();
        this.handlers.register(LoginRequest.class, this::login);
        this.handlers.register(MoveReq.class, this::move);
        this.handlers.register(ImpatientWinRequest.class, this::impatientWin);
        this.handlers.register(GetMatchReq.class, this::getMatch);
        this.handlers.register(GetMyMatchesRequest.class, this::getMyMatches);
        this.handlers.register(GetGamesRequest.class, this::getGames);
        this.handlers.register(GetAccountReq.class, this::getAccount);
        this.handlers.register(GetAccountsReq.class, this::getAccounts);
        this.handlers.register(CreateMatchRequest.class, this::createMatch);
        this.handlers.register(AddParticipantReq.class, this::addParticipant);
        this.handlers.register(GetParticipantsReq.class, this::getParticipants);
        this.handlers.register(GetPendingReq.class, this::getPending);
        this.handlers.register(DecidePendingReq.class, this::decidePending);
        this.handlers.register(UpdateUserStatusRequest.class, this::approveUserReg);
        this.handlers.register(UpdateAccountReq.class, this::updateAccount);
        this.handlers.register(GetScoreSumsRequest.class, this::getScoreSums);
        this.handlers.register(GetMatchHistoryRequest.class, this::getMatchHistory);
        this.handlers.register(BeginLiveUpdateRequest.class, this::beginLiveUpdate);
        this.handlers.register(QuitNotification.class, this::quit);
        this.handlers.register(RegisterRequest.class, this::createAccount);
    }

    @Override
    public void run() {
        // NOTE: Vi kunne i princippet starte X tråde her, da model er trådsikret
        // med f.eks. matchMutexes, men skal testes bedre.
        handlerThreadProc();
    }

    private void handlerThreadProc() {
        while (true) {
            try {
                QueuedMessage fromQueue = incomingQueue.pull(10_000);
                if (fromQueue != null) {
                    handleMessage(fromQueue.message(), fromQueue.clientIdent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message request, int clientIdent) {
        System.out.println("Incoming " + request.head().bodyType() + " from clientIdent " + clientIdent);

        long tBegin = System.nanoTime();
        Object responseBody = getResponseForRequest(request.body(), request.head().jwt(), clientIdent);
        long tEnd = System.nanoTime();
        double millis = (double)(tEnd - tBegin) / (1000.0 * 1000.0);

        if (responseBody != null) {
            Head responseHead = new Head(responseBody.getClass().getSimpleName(), "", millis);
            Message response = new Message(responseHead, responseBody);
            outgoingQueue.post(response, clientIdent);
        }
    }

    private Object getResponseForRequest(Object request, String jwt, int clientIdent) {
        MessageHandler<Object> h = handlers.get(request.getClass());
        if (h == null) {
            System.out.println("No handler for message type " + request.getClass().getSimpleName() + ".");
            return null; // Ukendt message type -> ignorer.
        }

        Object ret;
        try {
            ret = h.handle(request, jwt, clientIdent);
        } catch (NotAuthorizedException e) {
            ret = new NotAuthorizedResponse();
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////
    // Live update

    private BeginLiveUpdateResponse beginLiveUpdate(BeginLiveUpdateRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        listeners.remove(clientIdent); // Tilllad ikke samme klient at listen på flere accounts.
        listeners.put(claims.accountId(), clientIdent);
        return new BeginLiveUpdateResponse(true);
    }

    private Object quit(QuitNotification notification, String jwt, int clientIdent) {
        listeners.remove(clientIdent);
        return null; // Send ingen response.
    }

    private void broadcastMatchUpdate(int matchId) {
        Match m = matchService.get(matchId);
        if (m.matchId() == 0) {
            return;
        }

        // Broadcast to all clients that listen on each participating account.
        for (Participant p : m.participants()) {
            if (p.status() != Participant.STATUS_REJECTED) {
                MatchNotification response = new MatchNotification(matchId);
                Message message = new Message(response, 0.0);
                for (int listeningClientIdent : listeners.get(p.accountId())) {
                    outgoingQueue.post(message, listeningClientIdent);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // Authorization

    private LoginResponse login(LoginRequest req, String jwt, int clientIdent) {
        String hashedPassword = PasswordHashing.hash(req.password());
        Account account = accountService.get(req.username(), hashedPassword);
        if (account == null || (req.adminClient() && !account.isAdmin())) {
            return new LoginResponse(false, Empty.account(), "");
        } else {
            jwt = jwtService.create(account);
            return new LoginResponse(true, account, jwt);
        }
    }


    private RegisterResponse createAccount(RegisterRequest req, String jwt, int clientIdent) {
        //Der kunne muligvis laves bedre errorhandling
        if (req.username() == null || req.firstName() == null||req.password() == null||req.lastName() == null||req.username().isEmpty() || req.firstName().isEmpty()||req.password().isEmpty()||req.lastName().isEmpty())
            return new RegisterResponse(false, "No parameters can be empty");
        String hashedPassword = PasswordHashing.hash(req.password());
        if (accountService.get(req.username()) != null)
            return new RegisterResponse(false, "Username Already taken");
        RegisterAccountParam param = new RegisterAccountParam(req.username(),req.firstName(),req.lastName(),req.email(), hashedPassword);
        Account account = accountService.create(param);
        if (account == null ) {
            return new RegisterResponse(false, "Account could not be made");
        } else {
            return new RegisterResponse(true,"");
        }
    }

    ////////////////////////////////////////////////////////////////
    // Matches

    private GetMyMatchesResponse getMyMatches(GetMyMatchesRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Match> matches = new ArrayList<>();

        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_PENDING));
        matches.addAll(matchService.getAll(claims.accountId(), Match.STATUS_ONGOING));

        return new GetMyMatchesResponse(matches);
    }

    private CreateMatchResponse createMatch(CreateMatchRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);

        // Create
        CreateMatchParam param = new CreateMatchParam(claims.accountId(), req.gameId());
        Match match = matchService.create(param);

        CreateMatchResponse res = matchMutexes.lockedScope(match.matchId(), () -> {
            // Owner accepts automatically.
            AddParticipantReq addReq = new AddParticipantReq(match.matchId(), claims.accountId());
            AddParticipantRes addRes = addParticipant(addReq, jwt, clientIdent);
            DecidePendingReq decideReq = new DecidePendingReq(match.matchId(), addRes.participant().participantId(), Participant.STATUS_ACCEPTED);
            DecidePendingRes decideRes = decidePending(decideReq, jwt, clientIdent);

            return new CreateMatchResponse("", match);
        });

        broadcastMatchUpdate(match.matchId());
        return res;
    }

    ////////////////////////////////////////////////////////////////
    // Spilkatalog

    private GetGamesResponse getGames(GetGamesRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Game> games = gameService.getGames();
        return new GetGamesResponse(games);
    }

    private GetMatchRes getMatch(GetMatchReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        Match match = matchService.get(req.matchId());
        if (match == null) {
            match = Empty.match();
        }
        return new GetMatchRes(match);
    }

    ////////////////////////////////////////////////////////////////
    // Participants

    private AddParticipantRes addParticipant(AddParticipantReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        AddParticipantRes res = matchMutexes.lockedScope(req.matchId(), () -> {

            Claims claims = jwtService.verify(jwt);
            Match match = matchService.get(req.matchId());

            // Tjek match ikke er begyndt endnu.
            if (match.status() != Match.STATUS_PENDING) {
                return new AddParticipantRes(Empty.participant(), "Cannot add participant to an ongoing/finished game.");
            }

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
        });

        broadcastMatchUpdate(req.matchId());
        return res;
    }

    private GetParticipantsRes getParticipants(GetParticipantsReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Participant> participants = participantService.getByMatch(req.matchId());
        return new GetParticipantsRes(participants);
    }

    private GetPendingRes getPending(GetPendingReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        List<Participant> pending = participantService.getByAccountAndStatus(claims.accountId(), Participant.STATUS_PENDING);
        return new GetPendingRes(pending);
    }

    private DecidePendingRes decidePending(DecidePendingReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        DecidePendingRes res = matchMutexes.lockedScope(req.matchId(), () -> {

            Claims claims = jwtService.verify(jwt);

            Match match = matchService.get(req.matchId());
            TurnBasedGameLogic gl = GameCatalog.getLogic(match.gameId());
            GameSpec spec = gl.spec();

            // Tjek korrekt account.
            Participant participant = Participants.getById(match.participants(), req.participantId());
            if (participant.accountId() != claims.accountId()) {
                throw new NotAuthorizedException();
            }

            // Tillid ikke f.eks. at sætte status tilbage til pedngin.
            boolean validStatus = req.status() == Participant.STATUS_ACCEPTED ||
                                  req.status() == Participant.STATUS_REJECTED;

            if (validStatus) {
                participant.setStatus(req.status());
                participantService.update(participant);

                // Start match når der ikke er flere pending, og vi har nok deletagere.
                int pendingCount = Participants.countByStatus(match.participants(), Participant.STATUS_PENDING);
                int acceptedCount = Participants.countByStatus(match.participants(), Participant.STATUS_ACCEPTED);
                if (pendingCount == 0 && acceptedCount == spec.needPlayerCount()) {
                    var players = Participants.withoutStatus(match.participants(), Participant.STATUS_REJECTED);
                    var data = gl.getInitialData(players);
                    match.setData(data);
                    match.setNextAccountId(match.ownerId());
                    match.setStatus(Match.STATUS_ONGOING);
                    match.setStartedOn(LocalDateTime.now());
                    matchService.update(match);
                }

                return new DecidePendingRes("");
            } else {
                return new DecidePendingRes("Invalid participant status (was " + req.status() + " but must be ACCEPTED or REJECTED).");
            }
        });

        broadcastMatchUpdate(req.matchId());
        return res;
    }

    ////////////////////////////////////////////////////////////////
    // Move

    private MoveRes move(MoveReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        MoveRes res = matchMutexes.lockedScope(req.matchId(), () -> {

            Claims claims = jwtService.verify(jwt);
            Account account = accountService.get(claims.accountId());
            Match match = matchService.get(req.matchId());

            if (match.status() != Match.STATUS_ONGOING) {
                return new MoveRes(match.matchId(), match.data(), MoveResult.invalid("Match is not ongoing"));
            }

            if (match.nextAccountId() != claims.accountId()) {
                return new MoveRes(match.matchId(), match.data(), MoveResult.invalid("It's not your turn."));
            }

            TurnBasedGameLogic gl = GameCatalog.getLogic(match.gameId());
            MoveResult result = gl.validateMoveAndUpdateData(req, match);

            applyMoveResultToMatch(result, match);

            MoveRes response = new MoveRes(match.matchId(), match.data(), result);
            return response;
        });

        broadcastMatchUpdate(req.matchId());
        return res;
    }

    private ImpatientWinResponse impatientWin(ImpatientWinRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        ImpatientWinResponse res = matchMutexes.lockedScope(req.matchId(), () -> {
            final int MAX_HOURS_WAIT = 24;

            jwtService.verify(jwt);

            Match m = matchService.get(req.matchId());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime then = m.lastMoveOn();

            // Tjek om impatient win er lovlig.
            long hoursSinceLastMove = ChronoUnit.HOURS.between(then, now);
            if (hoursSinceLastMove < 24) {
                return new ImpatientWinResponse(
                    m.matchId(),
                    String.format(
                        "It's has been %d hours since last move, but must be %d since last move to do impatient win.",
                        hoursSinceLastMove, MAX_HOURS_WAIT
                    )
                );
            }

            // Update game status, sæt score osv.
            TurnBasedGameLogic gl = GameCatalog.getLogic(m.gameId());
            MoveResult moveResult = gl.impatientWin(m);
            applyMoveResultToMatch(moveResult, m);
            return new ImpatientWinResponse(m.matchId(), "");
        });

        broadcastMatchUpdate(req.matchId());
        return res;
    }

    private void applyMoveResultToMatch(MoveResult result, Match match) {
        switch (result.outcome()) {
            case MoveResult.OUTCOME_VALID -> {
                match.setData(result.nextData());
                match.setNextAccountId(result.nextAccountId());
                match.setLastMoveOn(LocalDateTime.now());
                matchService.update(match);
            }

            case MoveResult.OUTCOME_INVALID -> {
                // Opdater ikke noget i databasen.
            }

            case MoveResult.OUTCOME_FINISHED -> {
                match.setStatus(Match.STATUS_FINISHED);
                match.setLastMoveOn(LocalDateTime.now());
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
    }

    ////////////////////////////////////////////////////////////////
    // Accounts

    private GetAccountRes getAccount(GetAccountReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        jwtService.verify(jwt);
        Account account = accountService.get(req.accountId());
        GetAccountRes res = new GetAccountRes(account);
        return res;
    }

    private GetAccountsRes getAccounts(GetAccountsReq req, String jwt, int clientIdent) throws NotAuthorizedException {
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

    private UpdateUserStatusResponse approveUserReg(UpdateUserStatusRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        req.account().setStatus(req.newStatus());
        boolean success = accountService.update(req.account());
        return new UpdateUserStatusResponse(success);
    }

    private UpdateAccountRes updateAccount(UpdateAccountReq req, String jwt, int clientIdent) throws NotAuthorizedException {
        Claims claims = jwtService.verify(jwt);
        Account fromClient = req.account();
        Account fromServer = accountService.get(fromClient.accountId());
        Account withSameUsername = accountService.get(req.account().username());

        Validation v = new Validation();

        if (!claims.isAdmin()) {
            if (fromClient.accountId() != claims.accountId()) {
                v.reportInvalid(String.format("Tried to edit account id %s but jwt account id was %s.", fromClient.accountId(), claims.accountId()));
            }

            v.mustBeEqual(fromClient, fromServer, Account::registerDateTime, "register date time");
            v.mustBeEqual(fromClient, fromServer, Account::createdOn, "created on");
            v.mustBeEqual(fromClient, fromServer, Account::status, "status");

            v.mustBeNonEmpty(fromClient, Account::username, "username");
            v.mustBeNonEmpty(fromClient, Account::firstName, "first name");
            v.mustBeNonEmpty(fromClient, Account::lastName, "last name");
            v.mustBeNonEmpty(fromClient, Account::email, "email");

            v.mustBeShorterThan(fromClient, Account::description, "description", 500);
        }

        if (withSameUsername != null) {
            v.reportInvalid("Username already taken.");
        }

        if (v.isValid()) {
            accountService.update(fromClient);
            return new UpdateAccountRes("");
        } else {
            return new UpdateAccountRes(v.reason());
        }
    }

    ////////////////////////////////////////////////////////////////
    // Scores

    private GetScoreSumsResponse getScoreSums(GetScoreSumsRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        jwtService.verify(jwt);
        List<ScoreSum> sums = scoreService.getSums(req.gameId());
        GetScoreSumsResponse res = new GetScoreSumsResponse(sums);
        return res;
    }

    private GetMatchHistoryResponse getMatchHistory(GetMatchHistoryRequest req, String jwt, int clientIdent) throws NotAuthorizedException {
        jwtService.verify(jwt);
        List<FinishedMatchScore> scores = scoreService.getScores(req.accountId());
        GetMatchHistoryResponse res = new GetMatchHistoryResponse(scores);
        return res;
    }
}
