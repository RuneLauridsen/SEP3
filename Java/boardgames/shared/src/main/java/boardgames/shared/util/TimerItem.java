package boardgames.shared.util;

public record TimerItem(String name, long beginNano, long endNano, double elapsedMillis) { }
