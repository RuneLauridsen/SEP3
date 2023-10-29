package boardgames.game.services;

import org.springframework.web.client.RestTemplate;

public class GameServiceRest implements GameService {
    private final String ulr;
    private final RestTemplate restTemplate;

    public GameServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    // TODO(rune): List<Game> getGames()
}
