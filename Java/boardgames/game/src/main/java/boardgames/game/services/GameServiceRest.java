package boardgames.game.services;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Game;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GameServiceRest implements GameService {
    private final String ulr;
    private final RestTemplate restTemplate;

    public GameServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Game> getGames() {
        try {
            ResponseEntity<Game[]> response = restTemplate.getForEntity(ulr + "/games", Game[].class);
            return List.of(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
