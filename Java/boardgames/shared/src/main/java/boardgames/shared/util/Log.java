package boardgames.shared.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class Log {
    public enum Severity { INFO, ERROR }

    public static void logInfo(String string) {
        log(Severity.INFO, string);
    }

    public static void logError(String string) {
        log(Severity.ERROR, string);
    }

    public static void logError(Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        log(Severity.ERROR, stackTrace.toString());
    }

    public static void log(Severity severity, String string) {
        // Erstat med noget fancy, hvis du vil have en fancy log.
        System.out.printf(
            "[%s %s] %s\n",
            severity.toString(),
            LocalDateTime.now().toString(),
            string
        );
    }
}
