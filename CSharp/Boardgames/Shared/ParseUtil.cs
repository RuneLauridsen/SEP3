﻿namespace Shared;

public class ParseUtil {
    public static int ParseIntOrDefault(string s, int whenNotParsed = default) {
        if (int.TryParse(s, out var parsed)) {
            return parsed;
        } else {
            return whenNotParsed;
        }
    }

    public static int ParseIntOrDefault(object? o, int whenNotParsed = default) {
        return ParseIntOrDefault(o?.ToString() ?? "", default);
    }
}
