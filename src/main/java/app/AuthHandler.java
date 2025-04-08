package app;

import io.muserver.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

public class AuthHandler {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";
    private static final Set<String> validTokens = ConcurrentHashMap.newKeySet();

    public static class LoginHandler implements RouteHandler {
        @Override
        public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {
            String username = request.form().get("username");
            String password = request.form().get("password");

            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                String token = UUID.randomUUID().toString();
                validTokens.add(token);
                response.headers().set("Set-Cookie", "token=" + token + "; Path=/; HttpOnly");
                response.status(200);
                response.write("Login successful! Redirecting...");
            } else {
                response.status(401);
                response.write("Invalid credentials. Please try again.");
            }
        }
    }

    public static RouteHandler protectedHandler(RouteHandler actualHandler) {
        return (request, response, pathParams) -> {
            String token = request.headers().get("Cookie");
            boolean authorized = false;

            if (token != null && token.contains("token=")) {
                String val = token.split("token=")[1].split(";")[0];
                authorized = validTokens.contains(val);
            }

            if (!authorized) {
                response.status(401);
                response.write("Unauthorized. Please login as restaurant owner.");
                return;
            }

            actualHandler.handle(request, response, pathParams);
        };
    }

    public static class LogoutHandler implements RouteHandler {
        @Override
        public void handle(MuRequest request, MuResponse response, java.util.Map<String, String> pathParams) throws IOException {
            String cookie = request.headers().get("Cookie");

            if (cookie != null && cookie.contains("token=")) {
                String val = cookie.split("token=")[1].split(";")[0];
                validTokens.remove(val);
            }

            response.headers().set("Set-Cookie", "token=; Max-Age=0; Path=/; HttpOnly");
            response.status(200);
            response.write("Logged out successfully");
        }
    }

    public static Set<String> getValidTokens() {
        return validTokens;
    }
}
