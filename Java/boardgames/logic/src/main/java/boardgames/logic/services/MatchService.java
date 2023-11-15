package boardgames.logic.services;

import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.Match;

import java.util.List;

public interface MatchService {
    public Match get(int matchId);
    public Match create(CreateMatchParam param);
    public void update(Match match);
    public void delete(int matchId);
    public List<Match> getAll(int accountId, int statusId);
}
