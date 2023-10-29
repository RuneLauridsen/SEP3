package boardgames.shared.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {
    private static Gson gson;

    private static Gson gson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static String toJson(Object object) {
        String ret = gson().toJson(object, object.getClass());
        return ret;
    }

    public static <T> T fromJson(String json, Class<T> toType) throws JsonSyntaxException {
        T ret = gson().fromJson(json, toType);
        return ret;
    }
}
