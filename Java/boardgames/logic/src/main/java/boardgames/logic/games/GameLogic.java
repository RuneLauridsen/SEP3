package boardgames.logic.games;

import boardgames.logic.messages.Messages.*;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;
import boardgames.shared.dto.Participant;

import java.util.List;

// TODO(rune): Tænk lidt mere over hvordan vi abstracter scoring.
public interface GameLogic {
    public GameSpec spec();
    public String getInitialData(List<Participant> participants);
    public MoveResult validateMoveAndUpdateData(MoveReq req, Match match); // NOTE(rune): VMUD i daglig tale.
}
