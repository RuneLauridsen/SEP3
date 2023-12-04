package boardgames.logic.networking;

import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// NOTE(rune): Tilsvarende klass i Java/C# skal passe sammen.
public class StringStreamer {
    public static void sendString(DataOutputStream stream, String string) throws IOException {
        byte[] dataBytes = string.getBytes(StandardCharsets.UTF_8);
        byte[] prefixBytes = new byte[4];

        prefixBytes[0] = (byte)(dataBytes.length >> 0);
        prefixBytes[1] = (byte)(dataBytes.length >> 8);
        prefixBytes[2] = (byte)(dataBytes.length >> 16);
        prefixBytes[3] = (byte)(dataBytes.length >> 24);

        stream.write(prefixBytes);
        stream.write(dataBytes);
        stream.flush();
    }

    public static String readString(DataInputStream stream) throws IOException {
        byte[] prefixBytes = stream.readNBytes(4);
        if (prefixBytes.length != 4) {
            throw new IOException("End of stream.");
        }

        // NOTE(rune): & 0xff er nødvendig fordi Java byte primitiv er signed.
        int dataLength = 0;
        dataLength |= (int)((prefixBytes[0] & 0xff) << 0);
        dataLength |= (int)((prefixBytes[1] & 0xff) << 8);
        dataLength |= (int)((prefixBytes[2] & 0xff) << 16);
        dataLength |= (int)((prefixBytes[3] & 0xff) << 24);

        byte[] dataBytes = stream.readNBytes(dataLength);
        if (dataBytes.length != dataLength) {
            throw new IOException("End of stream.");
        }

        String s = new String(dataBytes, StandardCharsets.UTF_8);
        return s;
    }
}
