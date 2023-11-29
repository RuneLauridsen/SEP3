package boardgames.logic.services;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.ScoreSum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
            return List.of(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
