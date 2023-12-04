package boardgames.logic.games.tictactoe;

import boardgames.logic.games.TurnBasedGameLogic;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.Messages.MoveReq;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;
import boardgames.shared.dto.Participant;
import boardgames.shared.util.JsonUtil;

import java.util.List;

// TODO(rune): Oplagt til unit-test.
public class TicTacToeLogic implements TurnBasedGameLogic {
    private final static int NUM_PLAYERS = 2;
    private final static int NUM_SQUARES = 9;
    private final static int NUM_PIECES_PER_PLAYER = 3;
    private final GameSpec spec = new GameSpec("TicTacToe", 2);

    private final static int[] winPatterns = {
        0b001_001_001,
        0b010_010_010,
        0b100_100_100,
        0b000_000_111,
        0b000_111_000,
        0b111_000_000,
        0b100_010_001,
        0b001_010_100,
    };

    @Override
    public GameSpec spec() {
        return spec;
    }

    @Override
    public MoveResult validateMoveAndUpdateData(MoveReq req, Match match) {
        TicTacToeData data = JsonUtil.fromJson(match.data(), TicTacToeData.class);
        TicTacToeMove move = JsonUtil.fromJson(req.moveData(), TicTacToeMove.class);
        MoveResult result = ticTacToeLogic(data, move, match.nextAccountId());
        return result;
    }

    @Override
    public String getInitialData(List<Participant> participants) {
        // NOTE: Antager a model har tjekket efter korrekt antal participants.
        assert participants.size() == 2;

        TicTacToeData data = new TicTacToeData();
        data.accountIdO = participants.get(0).accountId();
        data.accountIdX = participants.get(1).accountId();
        data.piecesO = 0;
        data.piecesX = 0;
        data.remainingX = 3;
        data.remainingO = 3;
        data.moveCount = 0;

        String s = JsonUtil.toJson(data);
        return s;
    }

    public MoveResult ticTacToeLogic(TicTacToeData data, TicTacToeMove move, int accountId) {
        // Indlæs i array så vi kan indexere efter spiller.
        int[] pieces = {data.piecesO, data.piecesX};
        int[] remaining = {data.remainingO, data.remainingX};
        int[] accountIds = {data.accountIdO, data.accountIdX};

        int playerIndex = -1;

        // Er det O's eller X's tur?
        if (accountId == data.accountIdO) playerIndex = 0;
        if (accountId == data.accountIdX) playerIndex = 1;

        if (playerIndex == -1) {
            return MoveResult.invalid(String.format("Account id %d is not a player.", accountId));
        }

        // Sæt ikke brik udenfor 0...9
        if (move.placeOnIndex < 0 || move.placeOnIndex >= NUM_SQUARES) {
            return MoveResult.invalid(String.format("Place on index out of range (was %d, but must be between 0 and %d).", move.placeOnIndex, NUM_SQUARES));
        }

        int placeOnBit = 1 << move.placeOnIndex;

        // Er der er allerede en brik på feltet?
        if ((pieces[0] & placeOnBit) != 0) return MoveResult.invalid("Square is already occupied.");
        if ((pieces[1] & placeOnBit) != 0) return MoveResult.invalid("Square is already occupied.");

        // Flyt brik.
        if (remaining[playerIndex] == 0) {
            int takeFromBit = 1 << move.takeFromIndex;

            if (move.takeFromIndex < 0 || move.takeFromIndex >= NUM_SQUARES) {
                return MoveResult.invalid(String.format("Take from index out of range (was %d, but must be between 0 and %d).", move.takeFromIndex, NUM_SQUARES));
            }

            if (placeOnBit == takeFromBit) {
                return MoveResult.invalid("Cannot place and take from same square.");
            }

            if ((pieces[playerIndex] & takeFromBit) == 0) {
                return MoveResult.invalid("Cannot take from square.");
            }

            pieces[playerIndex] &= ~takeFromBit;
        } else {
            remaining[playerIndex]--;
        }

        // Sæt brik.
        pieces[playerIndex] |= placeOnBit;

        // Læs arrays tilbage.
        data.piecesO = pieces[0];
        data.remainingO = remaining[0];
        data.piecesX = pieces[1];
        data.remainingX = remaining[1];

        // Check for tre på stribe.
        for (int pattern : winPatterns) {
            if ((pieces[playerIndex] & pattern) == pattern) {
                MoveResult result = MoveResult.finished(data, "ez win");
                result.scores().put(accountIds[playerIndex], 3);
                return result;
            }
        }

        // Næste spillers tur.
        data.moveCount++;
        int nextTurnAccountId = accountIds[data.moveCount % NUM_PLAYERS];

        // All done -> valid move.
        return MoveResult.valid(nextTurnAccountId, data);
    }

    @Override
    public MoveResult impatientWin(Match match) {
        TicTacToeData data = JsonUtil.fromJson(match.data(), TicTacToeData.class);

        int wouldHaveWonOnTurn = data.moveCount + 1;
        int winnerId;
        if (wouldHaveWonOnTurn % NUM_PLAYERS == 0) {
            winnerId = data.accountIdO;
        } else {
            winnerId = data.accountIdX;
        }

        MoveResult result = MoveResult.finished(data, "Impatient win");
        result.scores().put(winnerId, 3);
        return result;
    }
}
