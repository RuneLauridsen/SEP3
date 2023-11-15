package boardgames.logic.tictactoe;

import boardgames.shared.dto.Account;

public class TicTacToe {
    // NOTE(rune): Skal passe med tilsvarende game.game_id i databasen.
    public static final int TAC_TAC_TOE_GAME_ID = 1;

    public static String checkMove(Account by, String prevState, String nextState) {
        char invalidChar = '\0';

        if (nextState.length() != 9) {
            return "Board must be 3x3";
        }

        for (char c : nextState.toCharArray()) {
            if (c != 'X' &&
                c != 'O' &&
                c != '.') {
                invalidChar = c;
                break;
            }
        }

        if (invalidChar != '\0') {
            return "Invalid character '" + invalidChar + "'";
        }

        return "";
    }
}
