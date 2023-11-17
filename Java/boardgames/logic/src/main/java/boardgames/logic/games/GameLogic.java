package boardgames.logic.games;

import boardgames.logic.messages.Messages.*;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.Match;

public interface GameLogic {
    public GameSpec getSpec();

    public String getInitialData(Match match);
    public MoveRes validateMoveAndUpdateData(MoveReq req, Match match, Account reqBy); // NOTE(rune): VMUD i daglig tale.
}
