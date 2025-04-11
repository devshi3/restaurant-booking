package app;

import app.service.AuthService;
import io.muserver.*;

import java.io.IOException;

public class OwnerLogoutHandler implements RouteHandler {
    private final AuthService authService;

    public OwnerLogoutHandler(AuthService authService) {
        this.authService = authService;
    }
    
    @Override
    public void handle(MuRequest request, MuResponse response, java.util.Map<String, String> pathParams) throws IOException {
        authService.invalidateToken(request.headers().get("Cookie"));
        response.headers().set("Set-Cookie", "token=; Max-Age=0; Path=/; HttpOnly");
        response.status(200);
        response.write("Logged out successfully.");
    }
}
