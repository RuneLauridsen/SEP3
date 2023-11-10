package boardgames.shared.util;

import com.google.gson.*;
import org.springframework.cglib.core.Local;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JsonUtil {
    private static Gson gson;

    private static Gson gson() {
        if (gson == null) {
            // NOTE(rune): https://stackoverflow.com/a/23800221.
            gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String s = json.getAsJsonPrimitive().getAsString();
                        LocalDateTime ldt = LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        return ldt;
                    }
                }).registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        String s = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        return new JsonPrimitive(s);
                    }
                })
                .create();
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
