package boardgames.logic.services;

import boardgames.shared.dto.FinishedMatchScore;
import boardgames.shared.dto.ScoreSum;
import boardgames.shared.util.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static boardgames.logic.services.RestUtil.getBodyOrThrow;

public class ScoreServiceRest implements ScoreService {
    private final String ulr;
    private final RestTemplate restTemplate;

    public ScoreServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<ScoreSum> getSums(int gameId) {
        try {
            ResponseEntity<ScoreSum[]> response = restTemplate.getForEntity(ulr + "/games/" + gameId + "/scores", ScoreSum[].class);
            return List.of(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.logError(e);
            return List.of();
        }
    }
    @Override
    public List<FinishedMatchScore> getScores(int accountId) {
        try {
            ResponseEntity<FinishedMatchScore[]> response = restTemplate.getForEntity(ulr + "/accounts/" + accountId + "/scores", FinishedMatchScore[].class);
            return List.of(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.logError(e);
            return List.of();
        }
    }
}
