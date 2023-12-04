using GameClient;

namespace Shared.Data;

// NOTE(rune): Tilsvarende klasse i Java/C# skal passe sammen.
public static class MessageSerializer {
    public static Message Deserialize(string s) {
        int idx = s.IndexOf('|');
        if (idx > 0 && idx < s.Length - 1) {
            string headString = s.Substring(0, idx);
            string bodyString = s.Substring(idx + 1);

            Head head = JsonUtil.FromJson<Head>(headString);
            string bodyTypeName = typeof(Messages).FullName + "+" + head.bodyType; // NOTE(rune): "+" betyder nested class.
            Type? bodyType = Type.GetType(bodyTypeName);
            if (bodyType == null) {
                throw new Exception("Invalid response body type from game server (body type was '" + head.bodyType + "').");
            }

            object body = JsonUtil.FromJson(bodyString, bodyType);
            return new Message(head, body);
        }
        else {
            throw new Exception("Invalid response from game server (could not find '|' marker).");
        }
    }

    public static string Serialize(Message m) {
        string headString = JsonUtil.ToJson(m.Head);
        string bodyString = JsonUtil.ToJson(m.Body);
        string s = headString + "|" + bodyString;
        return s;
    }
}
