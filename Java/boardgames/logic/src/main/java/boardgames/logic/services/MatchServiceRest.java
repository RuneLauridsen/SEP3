package boardgames.logic.services;

import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MatchServiceRest implements MatchService {
    private final String ulr;
    private final RestTemplate restTemplate;

    public MatchServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Match create(CreateMatchParam param) {
        try {
            ResponseEntity<Match> response = restTemplate.postForEntity(ulr + "/matches", param, Match.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public Match get(int matchId) {
        try {
            ResponseEntity<Match> response = restTemplate.getForEntity(ulr + "/matches/" + matchId, Match.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public void update(Match match) {
        try {
            restTemplate.put(ulr + "/matches/" + match.matchId(), match);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public void delete(int matchId) {
        try {
            restTemplate.delete(ulr + "/matches/" + matchId);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public List<Match> getByAccount(int accountId) {
        try {
            ResponseEntity<Match[]> response = restTemplate.getForEntity(ulr + "/matches?accountId=" + accountId, Match[].class);
            return List.of(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
