package boardgames.shared.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class Log {
    public enum Severity { INFO, ERROR, PROFILE }

    public static void info(String string) {
        msg(Severity.INFO, string);
    }

    public static void error(String string) {
        msg(Severity.ERROR, string);
    }

    public static void profile(String string) {
        msg(Severity.PROFILE, string);
    }

    public static void error(Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        msg(Severity.ERROR, stackTrace.toString());
    }

    public static void msg(Severity severity, String string) {
        // NOTE(rune): Erstat med noget fancy, hvis du vil have en fancy log.
        System.out.printf(
            "[%s %s] %s\n",
            severity.toString(),
            LocalDateTime.now().toString(),
            string
        );
    }
}
