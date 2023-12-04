package boardgames.logic.messages;

import java.util.ArrayDeque;
import java.util.Deque;

public class MessageQueue {
    private final Deque<QueuedMessage> queue;

    public MessageQueue() {
        this.queue = new ArrayDeque<>();
    }

    public void post(QueuedMessage queuedMessage) {
        synchronized (queue) {
            queue.add(queuedMessage);
            queue.notifyAll();
        }
    }

    public void post(Message message, int clientIdent) {
        post(new QueuedMessage(message, clientIdent));
    }

    public QueuedMessage pull(long timeout) {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    queue.wait(timeout, 0);
                } catch (InterruptedException e) {

                }
            }

            return queue.remove();
        }
    }
}
