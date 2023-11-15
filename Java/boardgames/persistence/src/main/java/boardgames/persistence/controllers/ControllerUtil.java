package boardgames.persistence.controllers;

import boardgames.persistence.exceptions.BadRequestException;
import boardgames.persistence.exceptions.NotFoundException;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

/*
    NOTE(rune): CRUD ordbog

    Java        HTTP        SQL
    ------------------------------
    create      post        insert
    get         get         select
    update      put         update
    delete      delete      delete

 */

public class ControllerUtil {
    public static void throwIfNotFound(String username, Account account) throws NotFoundException {
        if (account == null) {
            throw new NotFoundException("Account with username '" + username + "' not found.");
        }
    }

    public static void throwIfNotFound(int accountId, Account account) throws NotFoundException {
        if (account == null) {
            throw new NotFoundException("Account with id " + accountId + " not found.");
        }
    }

    public static void throwIfNotFound(int matchId, Match match) throws NotFoundException {
        if (match == null) {
            throw new NotFoundException("Match with id " + matchId + " not found.");
        }
    }

    public static void throwIfNotFound(int gameId, Game game) throws NotFoundException {
        if (game == null) {
            throw new NotFoundException("Game with id " + gameId + " not found.");
        }
    }

    public static void throwIfNotFound(int participantId, Participant participant) throws NotFoundException {
        if (participant == null) {
            throw new NotFoundException("Participant with id " + participantId + " not found.");
        }
    }

    public static void throwIfMismatched(int matchId, Match match) throws BadRequestException {
        if (matchId != match.matchId()) {
            throw new BadRequestException("Match id in path (" + match + ") does not match match id in body (" + match.matchId() + ").");
        }
    }

    public static void throwIfMismatched(int participantId, Participant participant) throws BadRequestException {
        if (participantId != participant.participantId()) {
            throw new BadRequestException("Participant id in path (" + participant + ") does not match participant id in body (" + participant.participantId() + ").");
        }
    }
}
