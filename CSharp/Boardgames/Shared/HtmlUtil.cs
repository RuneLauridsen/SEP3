namespace Shared;

public static class HtmlUtil {
    public static string ToImgSrcString(string? base64, string? contentType) {
        if (base64 != null && contentType != null) {
            return string.Format("data:{0};base64, {1}", contentType, base64);
        } else {
            return "";
        }
    }
}
