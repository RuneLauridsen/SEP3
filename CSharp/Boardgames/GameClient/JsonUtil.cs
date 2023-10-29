using System.Text.Json;
using System.Transactions;

namespace GameClient;

public static class JsonUtil {
    private static readonly JsonSerializerOptions options = new() {
        PropertyNameCaseInsensitive = true
    };

    public static string ToJson<T>(T t) {
        string ret = JsonSerializer.Serialize(t, options);
        return ret;
    }

    public static T FromJson<T>(string json) {
        T ret = JsonSerializer.Deserialize<T>(json, options)!;
        return ret;
    }

    public static object FromJson(string json, Type type) {
        object ret = JsonSerializer.Deserialize(json, type, options)!;
        return ret;
    }
}
