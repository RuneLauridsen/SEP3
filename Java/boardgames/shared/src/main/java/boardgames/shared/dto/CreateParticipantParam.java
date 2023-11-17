package boardgames.shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// WARNING(rune): Navne skal matche mellem Java og C#.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateParticipantParam {
    private int matchId;
    private int accountId;

    public CreateParticipantParam() {
    }

    public CreateParticipantParam(int accountId, int matchId) {
        this.matchId = matchId;
        this.accountId = accountId;
    }

    public int matchId() {
        return matchId;
    }

    public int accountId() {
        return accountId;
    }
}
