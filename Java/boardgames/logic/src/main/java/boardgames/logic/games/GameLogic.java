package boardgames.logic.games;

import boardgames.logic.messages.Messages.*;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;

// TODO(rune): Tænk lidt mere over hvordan vi abstracter scoring.
public interface GameLogic {
    public GameSpec spec();
    public String getInitialData(Match match);
    public MoveResult validateMoveAndUpdateData(MoveReq req, Match match, Account reqBy); // NOTE(rune): VMUD i daglig tale.
}
