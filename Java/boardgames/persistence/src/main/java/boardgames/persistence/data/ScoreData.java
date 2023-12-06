package boardgames.persistence.data;

import boardgames.shared.dto.FinishedMatchScore;
import boardgames.shared.dto.ScoreSum;

import java.util.List;

public interface ScoreData {
    public List<ScoreSum> getSums(int gameId); // TODO(rune): Paging?
    public List<FinishedMatchScore> getScores(int accountId); // TODO(rune): Paging?
}
