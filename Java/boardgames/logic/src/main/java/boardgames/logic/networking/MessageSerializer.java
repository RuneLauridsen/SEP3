package boardgames.logic.networking;

import boardgames.logic.messages.Message;
import boardgames.logic.messages.Messages;
import boardgames.shared.util.JsonUtil;
import boardgames.shared.util.Log;
import com.google.gson.JsonSyntaxException;

// NOTE(rune): Tilsvarende klasse i Java/C# skal passe sammen.
public class MessageSerializer {
    public static Message deserialize(String s) {
        int idx = s.indexOf('|');
        if (idx > 0 && idx < s.length() - 1) {
            String headString = s.substring(0, idx);
            String bodyString = s.substring(idx + 1);
            try {
                Messages.Head head = JsonUtil.fromJson(headString, Messages.Head.class);
                String bodyTypeName = Messages.class.getName() + "$" + head.bodyType(); // NOTE(rune): "$" betyder nested class.
                Class<?> bodyType = Class.forName(bodyTypeName);
                Object body = JsonUtil.fromJson(bodyString, bodyType);
                return new Message(head, body);
            } catch (JsonSyntaxException | ClassNotFoundException e) {
                Log.error(e);
                return null;
            }
        } else {
            Log.error("Invalid message string received (could not find '|' marker). Message string was '" + s + "'");
            return null;
        }
    }

    public static String serialize(Message m) {
        String headString = JsonUtil.toJson(m.head());
        String bodyString = JsonUtil.toJson(m.body());
        String s = headString + "|" + bodyString;
        return s;
    }
}
