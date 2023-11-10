package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

import java.util.List;

// TODO(rune): Er DataAccess det rigtige term?
// TODO(rune): Interface segregation?
public interface DataAccess {

    //
    // Games
    //

    public Game getGame(int gameId);
    public List<Game> getGames();

    // TODO(rune): Behøver vel ikke nogen create osv. da vi kun har et fast katalog?

    //
    // Accounts
    //

    public Account getAccount(int accountId);
    public Account getAccount(String username);
    public Account getAccount(String username, String hashedPassword);
    // TODO(rune): updateAccount()
    // TODO(rune): deleteAccount()

    //
    // Matches
    //

    public Match getMatch(int matchId);
    public List<Match> getMatchesByAccount(Account account);
    public Match createMatch(Account owner, Game game);
    public int updateMatch(Match match);
    public int deleteMatch(int matchId);

    //
    // Participants
    //

    public Participant getParticipant(int participantId);
    public List<Participant> getParticipants(int matchId, int accountId, int participantStatus); // NOTE(rune): -1 hvis filter skal ignorers.
    public Participant createParticipant(Account account, Match match, int participantStatus);
    public int updateParticipant(Participant participant);
    public int deleteParticipant(int participantId);
}
