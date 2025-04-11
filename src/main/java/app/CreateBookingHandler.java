package app;

import io.muserver.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import app.model.Booking;
import app.repository.BookingRepository;

public class CreateBookingHandler implements RouteHandler {
    private final BookingRepository repository;

    public CreateBookingHandler(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {
        String customerName = request.form().get("customerName");
        int tableSize = request.form().getInt("tableSize", -1);
        String date = request.form().get("date");
        String time = request.form().get("time");

        Booking booking = new Booking();
        booking.setCustomerName(customerName);
        booking.setTableSize(tableSize);
        booking.setDate(date);
        booking.setTime(time);

        if (!booking.isValid()) {
            response.status(400);
            response.write("Invalid booking details.");
            return;
        }

        LocalDate bookingDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);

        if (bookingDate.isBefore(today)) {
            response.status(400);
            response.write("Cannot book a past date.");
            return;
        }

        if (bookingDate.isAfter(oneMonthLater)) {
            response.status(400);
            response.write("Bookings can only be made up to one month in advance.");
            return;
        }

        List<String> allowedTimes = Arrays.asList("10:00", "12:00", "14:00", "16:00", "18:00", "20:00");
        if (!allowedTimes.contains(time)) {
            response.status(400);
            response.write("Invalid time selected. Please choose a valid time slot.");
            return;
        }

        int MAX_CAPACITY = 100;
        int currentCapacity = repository.getCapacityForSlot(date, time);
        int seatsRemaining = MAX_CAPACITY - currentCapacity;

        if (tableSize > seatsRemaining) {
            response.status(400);
            response.write("Booking denied. Only " + seatsRemaining + " seats remaining for this time slot.");
            return;
        }

        String bookingId = UUID.randomUUID().toString();
        booking.setUuid(bookingId);
        repository.save(bookingId, booking);

        response.status(200);
        response.contentType("text/plain");
        response.write(
            "Booking Confirmed! ID: " + bookingId +
            "\nName: " + customerName +
            "\nDate: " + date +
            "\nTime: " + time +
            "\nTable Size: " + tableSize
        );
    }
}
