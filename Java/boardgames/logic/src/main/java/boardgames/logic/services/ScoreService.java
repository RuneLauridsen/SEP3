package boardgames.logic.services;

import boardgames.shared.dto.ScoreSum;

import java.util.List;

public interface ScoreService {
    public List<ScoreSum> getSums(int gameId);
}
