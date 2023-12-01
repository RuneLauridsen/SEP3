package boardgames.logic.games.tictactoe;

import boardgames.logic.games.TurnBasedGameLogic;
import boardgames.logic.games.GameSpec;
import boardgames.logic.messages.Messages.MoveReq;
import boardgames.shared.dto.Match;
import boardgames.shared.dto.MoveResult;
import boardgames.shared.dto.Participant;
import boardgames.shared.util.JsonUtil;
import boardgames.shared.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO(rune): Oplagt til unit-test.
public class TicTacToeLogic implements TurnBasedGameLogic {
    private final static int NUM_PLAYERS = 2;
    private final static int NUM_SQUARES = 9;
    private final static int NUM_PIECES_PER_PLAYER = 3;

    private final List<char[]> winPatterns;
    private final GameSpec spec;

    public TicTacToeLogic() {
        String raw = ResourceUtil.readResourceAsString(TicTacToeLogic.class, "/TicTacToeWinPatterns.txt");
        String withoutWhitespace = raw.replaceAll("\\s", "");
        winPatterns = new ArrayList<>();
        for (int i = 0; i < withoutWhitespace.length(); i += NUM_SQUARES) {
            char[] pattern = withoutWhitespace.substring(i, i + NUM_SQUARES).toCharArray();
            winPatterns.add(pattern);
        }
        spec = new GameSpec("TicTacToe", 2);
    }

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
        // TODO(rune): Check participants.size() indenfor spec.min()/spec.max()

        TicTacToePlayer[] players = new TicTacToePlayer[NUM_PLAYERS];
        players[0] = new TicTacToePlayer(participants.get(0).accountId(), 'X', NUM_PIECES_PER_PLAYER);
        players[1] = new TicTacToePlayer(participants.get(1).accountId(), 'O', NUM_PIECES_PER_PLAYER);

        char[] squares = new char[NUM_SQUARES];
        Arrays.fill(squares, ' ');

        TicTacToeData data = new TicTacToeData(players, squares);
        String s = JsonUtil.toJson(data);
        return s;
    }

    public MoveResult ticTacToeLogic(TicTacToeData data, TicTacToeMove move, int accountId) {
        // TODO(rune): Re-factor. Svært at holde styr på state, når alle predicates står først -> brug else i stedet.
        // TODO(rune): Må vi gerne droppe getters og setters? Det er dumt og jeg savner '++', '--' og '='  :((((

        // Validate TicTacToeData.
        if (data.players().length != NUM_PLAYERS) return MoveResult.invalid(String.format("Number of players must be %d, but was %s.", NUM_PLAYERS, data.players().length));
        if (data.squares().length != NUM_SQUARES) return MoveResult.invalid(String.format("Number of squares must be %d, but was %s.", NUM_SQUARES, data.squares().length));

        TicTacToePlayer player = null;
        for (TicTacToePlayer it : data.players()) {
            if (it.accountId() == accountId) {
                player = it;
            }
        }

        if (player == null) return MoveResult.invalid(String.format("Next turn account id is %d, but is not in the players array.", accountId));

        // Validate TicTacToeMove.
        if (move.takeFromIndex() < 0 || move.takeFromIndex() >= NUM_SQUARES) return MoveResult.invalid(String.format("Take from index out of range (was %d, but must be between 0 and %d).", move.takeFromIndex(), NUM_SQUARES));
        if (move.placeOnIndex() < 0 || move.placeOnIndex() >= NUM_SQUARES) return MoveResult.invalid(String.format("Place on index out of range (was %d, but must be between 0 and %d).", move.placeOnIndex(), NUM_SQUARES));

        // Check valid move.
        if (data.squares()[move.placeOnIndex()] != ' ') return MoveResult.invalid("Square is already occupied.");
        if (data.squares()[move.takeFromIndex()] != player.team() && player.remainingPieces() <= 0) return MoveResult.invalid("Cannot take from square.");

        // Take piece.
        if (player.remainingPieces() <= 0) {
            data.squares()[move.takeFromIndex()] = ' ';
        } else {
            player.setRemainingPieces(player.remainingPieces() - 1);
        }

        // Place piece.
        data.squares()[move.placeOnIndex()] = player.team();

        // Check for win state.
        for (char[] pattern : winPatterns) {
            if (matchesPattern(player.team(), data.squares(), pattern)) {
                MoveResult result = MoveResult.finished(data, "ez win");
                for (TicTacToePlayer p : data.players()) {
                    if (p.accountId() == player.accountId()) {
                        result.scores().put(p.accountId(), 3); // 3 point til vinder.
                    } else {
                        result.scores().put(p.accountId(), 0); // 0 pointer til taber.
                    }
                }

                return result;
            }
        }

        // Move to next player.
        data.setMoveCount(data.moveCount() + 1);
        int nextTurnAccountId = data.players()[data.moveCount() % NUM_PLAYERS].accountId();

        // All done -> valid move.
        return MoveResult.valid(nextTurnAccountId, data);
    }

    @Override
    public MoveResult impatientWin(Match match) {
        TicTacToeData data = JsonUtil.fromJson(match.data(), TicTacToeData.class);

        int wouldHaveWonOnTurn = data.moveCount() + 1;
        int winnerId = data.players()[wouldHaveWonOnTurn % NUM_PLAYERS].accountId();

        MoveResult result = MoveResult.finished(data, "Impatient win");
        for (TicTacToePlayer p : data.players()) {
            if (p.accountId() == winnerId) {
                result.scores().put(p.accountId(), 3); // 3 point til vinder.
            } else {
                result.scores().put(p.accountId(), 0); // 0 pointer til taber.
            }
        }

        return result;
    }

    private static boolean matchesPattern(char c, char[] s, char[] pattern) {
        assert s.length == NUM_SQUARES;
        assert pattern.length == NUM_SQUARES;

        for (int i = 0; i < NUM_SQUARES; i++) {
            if (pattern[i] == '#' && s[i] != c) {
                return false;
            }
        }

        return true;
    }
}
