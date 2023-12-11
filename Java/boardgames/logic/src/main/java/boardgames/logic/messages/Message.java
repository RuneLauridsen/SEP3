package boardgames.logic.messages;

import static boardgames.logic.messages.Messages.*;

public record Message(Head head, Object body) {
    public Message(Object body, double serverMillis) {
        this(new Head(body.getClass().getSimpleName(), "", serverMillis), body);
    }

    public Message(Object body) {
        this(body, 0.0);
    }
}
