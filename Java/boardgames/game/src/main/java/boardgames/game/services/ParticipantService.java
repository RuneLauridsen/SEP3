package boardgames.game.services;

import boardgames.shared.dto.CreateParticipantParam;
import boardgames.shared.dto.Participant;

import java.util.List;

public interface ParticipantService {
    public List<Participant> get(int matchId);
    public Participant create(CreateParticipantParam param);
    public void update(Participant participant);
    public void delete(int participantId);
}
