package boardgames.shared.util;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {

    // TODO(rune): Der må være en bedre måde...
    public static String readResourceAsString(Class<?> c, String resourceName) {
        InputStream stream = null;
        try {
            stream = c.getResourceAsStream(resourceName);
            if (stream == null) {
                throw new RuntimeException("Could not read resource '" + resourceName + "'.");
            }

            byte[] bytes = stream.readAllBytes();
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
