package boardgames.logic.model;

import java.util.Objects;
import java.util.function.Function;

public class Validation {
    private final StringBuilder reason;

    public Validation() {
        reason = new StringBuilder();
    }

    public String reason() { return reason.toString(); }
    public boolean isValid() { return reason.isEmpty(); }
    public boolean isInvalid() { return !isValid(); }

    public void invalid(String toAppend) {
        if (!reason.isEmpty()) {
            reason.append('\n');
        }

        reason.append(toAppend);
    }

    public void throwIfInvalid() throws ValidationException {
        if (isInvalid()) {
            throw new ValidationException(reason());
        }
    }

    public <T, M> void equal(T fromClient, T fromServer, Function<T, M> selector, String name) throws NotAuthorizedException {
        M valueFromClient = selector.apply(fromClient);
        M valueFromServer = selector.apply(fromServer);

        if (!Objects.equals(valueFromClient, valueFromServer)) {
            invalid(String.format("Tried to update %s, but updating this field is not allowed.", name));
        }
    }

    public <T, M> void mustBeNonNull(T fromClient, Function<T, M> selector, String name) {
        M valueFromClient = selector.apply(fromClient);

        if (valueFromClient == null) {
            invalid(String.format("Tried to update %s to null, but this field must be non-null.", name));
        }
    }

    public <T> void mustBeNonEmpty(T fromClient, Function<T, String> selector, String name) {
        String valueFromClient = selector.apply(fromClient);

        if (valueFromClient == null) {
            invalid(String.format("Tried to update %s to null, but this field must be non-null.", name));
        } else if (valueFromClient.isEmpty()) {
            invalid(String.format("Tried to update %s to empty string, but this field must be non-empty.", name));
        }
    }

    public <T> void mustBeShortedThan(T fromClient, Function<T, String> selector, String name, int maxLength) {
        String valueFromClient = selector.apply(fromClient);

        if (valueFromClient == null) {
            invalid(String.format("Tried to update %s to null, but this field must be non-null.", name));
        } else if (valueFromClient.length() > maxLength) {
            invalid(String.format("Tried to update %s to a length of %d, but max length is %d.", name, valueFromClient.length(), maxLength));
        }
    }
}
