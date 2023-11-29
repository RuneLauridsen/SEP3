package boardgames.persistence.data;

import boardgames.shared.dto.ScoreSum;

import java.util.List;

public interface ScoreData {
    public List<ScoreSum> getSums(int gameId);
}
