package boardgames.logic.games;

import boardgames.logic.messages.Messages.*;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;
import boardgames.shared.dto.Participant;

import java.util.List;

public interface TurnBasedGameLogic {
    public GameSpec spec();
    public String getInitialData(List<Participant> participants);
    public MoveResult validateMoveAndUpdateData(MoveRequest req, Match match); // NOTE(rune): VMUD i daglig tale.
    public MoveResult impatientWin(Match match);
}
