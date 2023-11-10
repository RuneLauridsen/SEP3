package boardgames.shared.dto;

public class CreateParticipantParam {
    private int matchId;
    private int accountId;

    public CreateParticipantParam() {
    }

    public CreateParticipantParam(int accountId, int matchId) {
        this.matchId = matchId;
        this.accountId = accountId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getAccountId() {
        return accountId;
    }
}
