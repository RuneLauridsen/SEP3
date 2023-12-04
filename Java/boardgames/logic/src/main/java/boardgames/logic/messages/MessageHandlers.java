package boardgames.logic.messages;

import java.util.HashMap;
import java.util.Map;

// NOTE(rune): Dispatch tabel til message typer.
public class MessageHandlers {
    private final Map<Class<?>, MessageHandler<?>> map;

    public MessageHandlers() {
        map = new HashMap<>();
    }

    public <TRequest> void register(Class<TRequest> type, MessageHandler<TRequest> handler) {
        map.put(type, handler);
    }

    public MessageHandler<Object> get(Class<?> type) {
        MessageHandler<Object> handler = (MessageHandler<Object>)map.get(type);
        return handler;
    }
}
