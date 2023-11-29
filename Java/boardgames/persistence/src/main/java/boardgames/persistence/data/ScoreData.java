package boardgames.persistence.data;

import boardgames.shared.dto.MatchScore;
import boardgames.shared.dto.ScoreSum;

import java.util.List;

public interface ScoreData {
    public List<ScoreSum> getSums(int gameId); // TODO(rune): Paging?
    public List<MatchScore> getScores(int accountId); // TODO(rune): Paging?
}
