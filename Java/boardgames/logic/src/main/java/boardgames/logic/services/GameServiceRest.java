package boardgames.logic.services;

import boardgames.shared.dto.Game;
import boardgames.shared.util.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static boardgames.logic.services.RestUtil.getBodyOrThrow;

public class GameServiceRest implements GameService {
    private final String url;
    private final RestTemplate restTemplate;

    public GameServiceRest(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Game> getGames() {
        try {
            ResponseEntity<Game[]> response = restTemplate.getForEntity(url + "/games", Game[].class);
            return List.of(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.error(e);
            return List.of();
        }
    }
}
