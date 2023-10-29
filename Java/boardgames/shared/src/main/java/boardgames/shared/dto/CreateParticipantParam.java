package boardgames.shared.dto;

public class CreateParticipantParam {
    private int matchId;
    private int accountId;
    private boolean accepted;

    public CreateParticipantParam(){
    }

    public CreateParticipantParam(int accountId, int matchId, boolean accepted) {
        this.matchId = matchId;
        this.accountId = accountId;
        this.accepted = accepted;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getAccountId() {
        return accountId;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
