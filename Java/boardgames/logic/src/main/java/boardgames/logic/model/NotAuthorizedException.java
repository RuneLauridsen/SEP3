package boardgames.logic.model;

public class NotAuthorizedException extends Throwable {
    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
