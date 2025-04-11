package app;

import app.service.AuthService;
import io.muserver.*;

import java.io.IOException;

public class OwnerLogoutHandler implements RouteHandler {
    @Override
    public void handle(MuRequest request, MuResponse response, java.util.Map<String, String> pathParams) throws IOException {
        AuthService.invalidateToken(request.headers().get("Cookie"));
        response.headers().set("Set-Cookie", "token=; Max-Age=0; Path=/; HttpOnly");
        response.status(200);
        response.write("Logged out successfully.");
    }
}
