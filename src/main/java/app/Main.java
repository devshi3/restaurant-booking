package app;

import io.muserver.*;
import io.muserver.handlers.ResourceHandlerBuilder;

import java.nio.file.Paths;

import static io.muserver.MuServerBuilder.httpServer;

public class Main {
    public static void main(String[] args) {
        BookingRepository repository = new BookingRepository();

        MuServer server = httpServer()
            .withHttpPort(8080)
            .addHandler(ResourceHandlerBuilder.fileHandler(Paths.get("static")))

            .addHandler(Method.POST, "/booking", new CreateBookingHandler(repository))
            .addHandler(Method.GET, "/bookings", new GetAllBookingsHandler(repository))
            .addHandler(Method.POST, "/login", new AuthHandler.LoginHandler())
            .addHandler(Method.POST, "/logout", new AuthHandler.LogoutHandler())

            .start();

        System.out.println("Server started at " + server.uri());
    }
}
