package boardgames.logic.networking;

import boardgames.logic.model.NotAuthorizedException;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlers {
    private final Map<Class<?>, Handler<?>> map;

    public MessageHandlers() {
        map = new HashMap<>();
    }

    public <TRequest> void register(Class<TRequest> type, Handler<TRequest> handler) {
        map.put(type, handler);
    }

    public Handler<Object> get(Class<?> type) {
        Handler<Object> handler = (Handler<Object>) map.get(type);
        return handler;
    }

    public static interface Handler<TRequest> {
        public Object handle(TRequest request, String jwt) throws NotAuthorizedException;
    }
}
