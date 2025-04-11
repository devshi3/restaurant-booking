package app;

import app.model.Owner;
import app.service.AuthService;
import io.muserver.*;

import java.io.IOException;
import java.util.Map;

public class OwnerLoginHandler implements RouteHandler {
    private final AuthService authService;

    public OwnerLoginHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {
        Owner owner = new Owner(request.form().get("username"), request.form().get("password"));

        if (authService.authenticate(owner.getUsername(), owner.getPassword())) {
            String token = authService.generateToken();
            response.headers().set("Set-Cookie", "token=" + token + "; Path=/; HttpOnly");
            response.status(200);
            response.write("Login successful! Redirecting...");
        } else {
            response.status(401);
            response.write("Invalid credentials.");
        }
    }
}
