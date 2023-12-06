package boardgames.logic.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

public class RestUtil {
    public static <T> T getBodyOrThrow(ResponseEntity<T> r) {
        T body = r.getBody();

        if (body == null) {
            throw new RestClientException("Response body was null.");
        }

        return body;
    }
}
