package boardgames.persistence.controllers;

import boardgames.persistence.data.ScoreData;
import boardgames.shared.dto.FinishedMatchScore;
import boardgames.shared.dto.ScoreSum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.throwIfNotFound;

@RestController
public class ScoreController {
    private final ScoreData scoreData;

    public ScoreController(ScoreData scoreData) {
        this.scoreData = scoreData;
    }

    @GetMapping("games/{gameId}/scores")
    public List<ScoreSum> getSums(@PathVariable int gameId) {
        List<ScoreSum> sums = scoreData.getSums(gameId);
        return sums;
    }

    @GetMapping("accounts/{accountId}/scores")
    public List<FinishedMatchScore> getScores(@PathVariable int accountId) {
        List<FinishedMatchScore> scores = scoreData.getScores(accountId);
        return scores;
    }
}
