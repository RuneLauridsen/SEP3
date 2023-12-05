package boardgames.logic.services;

import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MatchServiceRest implements MatchService {
    private final String url;
    private final RestTemplate restTemplate;

    public MatchServiceRest(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Match create(CreateMatchParam param) {
        try {
            ResponseEntity<Match> response = restTemplate.postForEntity(url + "/matches", param, Match.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public Match get(int matchId) {
        try {
            ResponseEntity<Match> response = restTemplate.getForEntity(url + "/matches/" + matchId, Match.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public void update(Match match) {
        try {
            restTemplate.put(url + "/matches/" + match.matchId(), match);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    public void delete(int matchId) {
        try {
            restTemplate.delete(url + "/matches/" + matchId);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public List<Match> getAll(int accountId, int status) {
        try {
            ResponseEntity<Match[]> response = restTemplate.getForEntity(url + "/matches?accountId=" + accountId + "&status=" + status, Match[].class);
            return List.of(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
