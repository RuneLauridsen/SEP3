package boardgames.game.services;

import boardgames.shared.dto.CreateMatchParam;
import boardgames.shared.dto.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.List;

public interface MatchService {
    public Match get(int matchId);
    public Match create(CreateMatchParam param);
    public void update(Match match);
    public void delete(int matchId);
    public List<Match> getByAccount(int accountId);
}
