package boardgames.game.services;

import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ParticipantServiceRest implements ParticipantService {
    private final String ulr;
    private final RestTemplate restTemplate;

    public ParticipantServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Participant> get(int matchId) {
        try {
            ResponseEntity<Participant[]> response = restTemplate.getForEntity(ulr + "/matches/" + matchId + "/participants", Participant[].class);
            return Arrays.asList(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public Participant create(CreateParticipantParam param) {
        try {
            ResponseEntity<Participant> response = restTemplate.postForEntity(ulr + "/matches/" + param.getMatchId() + "/participants", param, Participant.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public void update(Participant participant) {
        try {
            restTemplate.put(ulr + "/participants/" + participant.getParticipantId(), participant);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public void delete(int participantId) {
        try {
            restTemplate.delete(ulr + "/matches/" + participantId);
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
