package boardgames.logic.messages;

import static boardgames.logic.messages.Messages.*;

public class Message {
    private final Head head;
    private final Object body;

    public Message(Head head, Object body) {
        this.head = head;
        this.body = body;
    }

    public Message(Object body, double serverMillis) {
        this.head = new Head(body.getClass().getSimpleName(), "", serverMillis);
        this.body = body;
    }

    public Head head() { return this.head; }
    public Object body() { return this.body; }
}
