using System.Net.Sockets;
using System.Text;

namespace Shared.Data;

// NOTE(rune): Tilsvarende klasse i Java/C# skal passe sammen.
public static class StringStreamer {
    public static async Task SendStringAsync(Stream stream, string s, CancellationToken ct) {
        byte[] dataBytes = Encoding.UTF8.GetBytes(s);
        byte[] prefixBytes = new byte[4];

        prefixBytes[0] = (byte)(dataBytes.Length >> 0);
        prefixBytes[1] = (byte)(dataBytes.Length >> 8);
        prefixBytes[2] = (byte)(dataBytes.Length >> 16);
        prefixBytes[3] = (byte)(dataBytes.Length >> 24);

        await stream.WriteAsync(prefixBytes, ct);
        await stream.WriteAsync(dataBytes, ct);
        await stream.FlushAsync(ct);
    }

    private static async Task<byte[]> ReadBytesAsync(Stream stream, int count, CancellationToken ct) {
        byte[] ret = new byte[count];
        int totalBytesRead = 0;
        do {
            int bytesRead = await stream.ReadAsync(ret, totalBytesRead, count, ct);
            if (bytesRead == 0) {
                break;
            }
            totalBytesRead += bytesRead;
            count -= bytesRead;

        } while (count > 0);

        if (totalBytesRead != ret.Length) {
            ret = ret.Take(totalBytesRead).ToArray();
        }

        return ret;
    }

    public static async Task<string> ReadStringAsync(NetworkStream stream, CancellationToken ct) {
        byte[] prefixBytes = await ReadBytesAsync(stream, 4, ct);

        int dataLength = 0;
        dataLength |= (int)(prefixBytes[0] << 0);
        dataLength |= (int)(prefixBytes[1] << 8);
        dataLength |= (int)(prefixBytes[2] << 16);
        dataLength |= (int)(prefixBytes[3] << 24);

        byte[] dataBytes = await ReadBytesAsync(stream, dataLength, ct);

        string s = Encoding.UTF8.GetString(dataBytes);
        return s;
    }
}
