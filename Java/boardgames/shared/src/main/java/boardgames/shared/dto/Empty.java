package boardgames.shared.dto;

import java.time.LocalDateTime;
import java.util.List;

// TODO(rune): Er det her Null-Object-Pattern???????????????
public class Empty {
    public static Account account() {
        return new Account(0, "?", "?", "?", "?", Empty.dateTime(), 0, Empty.dateTime());
    }

    public static Match match() {
        return new Match(0, "", 0, 0, Empty.dateTime());
    }

    public static Game game() {
        return new Game(0, "?");
    }

    public static Participant participant() {
        return new Participant(0, 0, 0, 0, Empty.dateTime());
    }

    public static LocalDateTime dateTime() {
        return LocalDateTime.of(1, 1, 1, 1, 1, 1);
    }

    public static <T> List<T> list() {
        return List.of();
    }
}
