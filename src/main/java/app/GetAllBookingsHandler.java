package app;

import io.muserver.*;
import com.google.gson.Gson;

import app.model.Booking;
import app.repository.BookingRepository;

import java.io.IOException;
import java.util.*;

public class GetAllBookingsHandler implements RouteHandler {

    private final BookingRepository repository;

    public GetAllBookingsHandler(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {
        Map<String, Map<String, List<Booking>>> groupedBookings = new HashMap<>();

        for (Booking booking : repository.getAll()) {
            groupedBookings
                .computeIfAbsent(booking.getDate(), k -> new HashMap<>())
                .computeIfAbsent(booking.getTime(), k -> new ArrayList<>())
                .add(booking);
        }

        response.contentType("application/json");
        response.write(new Gson().toJson(groupedBookings));
    }
}
