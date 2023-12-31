package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

import java.util.List;

public interface ParticipantData {
    public Participant get(int participantId);
    public List<Participant> getAll(int matchId, int accountId, int status); // NOTE(rune): -1 hvis filter skal ignorers.
    public Participant create(int matchId, int accountId, int status);
    public void update(Participant participant);
    public void delete(int participantId);
}
