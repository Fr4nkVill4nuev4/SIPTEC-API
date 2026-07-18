package SPITEC.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class TextUtils {
    private TextUtils() {
    }

    public static String cleanText(String value) {
        return value == null ? "" : value.trim();
    }

    public static String hashPassword(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte item : hash) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el hash de la contrasena", e);
        }
    }
}
