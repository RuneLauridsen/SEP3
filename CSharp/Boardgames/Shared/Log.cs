namespace Shared;

public static class Log {
    public enum Severity { INFO, ERROR }

    public static void Info(string s) {
        Msg(Severity.INFO, s);
    }

    public static void Error(string s) {
        Msg(Severity.ERROR, s);
    }

    public static void Error(Exception e) {
        Msg(Severity.ERROR, e.ToString());
    }

    public static void Msg(Severity severity, string s) {
        // NOTE(rune): Erstat med noget fancy, hvis du vil have en fancy Log.
        Console.WriteLine($"[{severity.ToString()} {DateTime.Now:yyyy-MM-dd hh:mm:ss.fff}] {s}\n");
    }
}
