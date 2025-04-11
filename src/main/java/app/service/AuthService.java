package app.service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private static final Set<String> validTokens = ConcurrentHashMap.newKeySet();

    public static String generateToken() {
        String token = UUID.randomUUID().toString();
        validTokens.add(token);
        return token;
    }

    public static boolean isValidToken(String cookieHeader) {
        if (cookieHeader != null && cookieHeader.contains("token=")) {
            String token = cookieHeader.split("token=")[1].split(";")[0];
            return validTokens.contains(token);
        }
        return false;
    }

    public static void invalidateToken(String cookieHeader) {
        if (cookieHeader != null && cookieHeader.contains("token=")) {
            String token = cookieHeader.split("token=")[1].split(";")[0];
            validTokens.remove(token);
        }
    }
}
