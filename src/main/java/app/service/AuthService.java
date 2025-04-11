package app.service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final Set<String> validTokens = ConcurrentHashMap.newKeySet();
    private final String validUsername = "owner";
    private final String validPassword = "password123"; // store this securely in real apps

    public boolean authenticate(String username, String password) {
        return validUsername.equals(username) && validPassword.equals(password);
    }

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        validTokens.add(token);
        return token;
    }

    public boolean isValidToken(String cookieHeader) {
        String token = extractToken(cookieHeader);
        return token != null && validTokens.contains(token);
    }

    public void invalidateToken(String cookieHeader) {
        String token = extractToken(cookieHeader);
        if (token != null) {
            validTokens.remove(token);
        }
    }

    private String extractToken(String cookieHeader) {
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            cookie = cookie.trim();
            if (cookie.startsWith("token=")) {
                return cookie.substring("token=".length());
            }
        }
        return null;
    }
}
