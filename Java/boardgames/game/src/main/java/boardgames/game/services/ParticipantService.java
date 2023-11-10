package boardgames.game.services;

import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Participant;

import java.util.List;

public interface ParticipantService {
    public Participant get(int participantId);
    public List<Participant> getByMatch(int matchId);
    public List<Participant> getByAccountAndStatus(int accountId, int participantStatus);
    public Participant create(CreateParticipantParam param);
    public void update(Participant participant);
    public void delete(int participantId);
}
