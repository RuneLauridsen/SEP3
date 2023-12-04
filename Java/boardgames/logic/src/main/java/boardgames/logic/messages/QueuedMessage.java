package boardgames.logic.messages;

public record QueuedMessage(Message message, int clientIdent) {
}
