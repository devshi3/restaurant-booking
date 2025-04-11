package app;

import io.muserver.*;
import io.muserver.handlers.ResourceHandlerBuilder;

import java.nio.file.Paths;

import app.repository.BookingRepository;
import app.service.AuthService;

import static io.muserver.MuServerBuilder.httpServer;

public class Main {
    public static void main(String[] args) {
        BookingRepository repository = new BookingRepository();

        MuServer server = httpServer()
            .withHttpPort(8080)
            .addHandler(ResourceHandlerBuilder.fileHandler(Paths.get("static")))

            .addHandler(Method.POST, "/booking", new CreateBookingHandler(repository))
            .addHandler(Method.GET, "/bookings", new GetAllBookingsHandler(repository))
            .addHandler(Method.POST, "/login", new OwnerLoginHandler())
            .addHandler(Method.POST, "/logout", new OwnerLogoutHandler())

            .addHandler(Method.GET, "/check-auth", (request, response, pathParams) -> {
                String cookieHeader = request.headers().get("Cookie");
                if (AuthService.isValidToken(cookieHeader)) {
                    response.status(200);
                    response.write("OK");
                } else {
                    response.status(401);
                    response.write("Unauthorized");
                }
            })
            
            .start();

        System.out.println("Server started at " + server.uri());
    }
}
