package boardgames.logic.services;

import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Participant;
import boardgames.shared.util.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static boardgames.logic.services.RestUtil.getBodyOrThrow;

public class ParticipantServiceRest implements ParticipantService {
    private final String url;
    private final RestTemplate restTemplate;

    public ParticipantServiceRest(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Participant get(int participantId) {
        try {
            ResponseEntity<Participant> response = restTemplate.getForEntity(url + "/participants/" + participantId, Participant.class);
            return getBodyOrThrow(response);
        } catch (RestClientException e) {
            Log.error(e);
            return null;
        }
    }

    @Override
    public List<Participant> getByMatch(int matchId) {
        try {
            ResponseEntity<Participant[]> response = restTemplate.getForEntity(url + "/participants?matchId=" + matchId, Participant[].class);
            return Arrays.asList(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.error(e);
            return List.of();
        }
    }

    @Override
    public List<Participant> getByAccountAndStatus(int accountId, int participantStatus) {
        try {
            ResponseEntity<Participant[]> response = restTemplate.getForEntity(url + "/participants?accountId=" + accountId + "&participantStatus=" + participantStatus, Participant[].class);
            return Arrays.asList(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.error(e);
            return List.of();
        }
    }

    @Override
    public Participant create(CreateParticipantParam param) {
        try {
            ResponseEntity<Participant> response = restTemplate.postForEntity(url + "/matches/" + param.matchId() + "/participants", param, Participant.class);
            return getBodyOrThrow(response);
        } catch (RestClientException e) {
            Log.error(e);
            return null;
        }
    }

    @Override
    public void update(Participant participant) {
        try {
            restTemplate.put(url + "/participants/" + participant.participantId(), participant);
        } catch (RestClientException e) {
            Log.error(e);
        }
    }

    @Override
    public void delete(int participantId) {
        try {
            restTemplate.delete(url + "/participants/" + participantId);
        } catch (RestClientException e) {
            Log.error(e);
        }
    }
}
