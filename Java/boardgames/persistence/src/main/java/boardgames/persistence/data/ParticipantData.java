package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.Participant;

import java.util.List;

public interface ParticipantData {
    public Participant get(int participantId);
    public List<Participant> getAll(int matchId, int accountId, int participantStatus); // NOTE(rune): -1 hvis filter skal ignorers.
    public Participant create(Account account, Match match, int participantStatus);
    public int update(Participant participant);
    public int delete(int participantId);
}
