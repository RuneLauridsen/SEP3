package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import boardgames.shared.dto.Match;

import java.util.List;

public interface MatchData {
    public Match get(int matchId);
    public List<Match> getAll(int accountId, int status);
    public Match create(Account owner, Game game);
    public int update(Match match);
    public int delete(int matchId);
}
