package boardgames.logic.model;

import java.awt.image.AreaAveragingScaleFilter;
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

    public <T, M> void mustBeEqual(T fromClient, T fromServer, Function<T, M> selector, String name) throws NotAuthorizedException {
        M valueFromClient = selector.apply(fromClient);
        M valueFromServer = selector.apply(fromServer);

        if (!Objects.equals(valueFromClient, valueFromServer)) {
            invalid(String.format("Tried to update %s.", name));
        }
    }

    public <T, M> void mustBeNonNull(T fromClient, Function<T, M> selector, String name) throws NotAuthorizedException {
        M valueFromClient = selector.apply(fromClient);

        if (valueFromClient == null) {
            invalid(String.format("Tried to update %s to null.", name));
        }
    }

    public <T> void mustBeNonEmpty(T fromClient, Function<T, String> selector, String name) throws NotAuthorizedException {
        String valueFromClient = selector.apply(fromClient);

        if (valueFromClient == null) {
            invalid(String.format("Tried to update %s to null.", name));
        } else if (valueFromClient.isEmpty()) {
            invalid(String.format("Tried to update %s to empty string.", name));
        }
    }
}
