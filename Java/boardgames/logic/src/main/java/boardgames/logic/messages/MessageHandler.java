package boardgames.logic.messages;

import boardgames.logic.model.NotAuthorizedException;

public interface MessageHandler<TRequest> {
    public Object handle(TRequest request, String jwt, int clientIdent) throws NotAuthorizedException;
}
