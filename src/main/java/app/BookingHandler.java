package app;

import io.muserver.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;

public class BookingHandler {

    private static final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private static final int MAX_CAPACITY = 100;

    public static class CreateBookingHandler implements RouteHandler {
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

            int currentCapacity = bookings.values().stream()
                    .filter(b -> b.getDate().equals(date) && b.getTime().equals(time))
                    .mapToInt(Booking::getTableSize)
                    .sum();

            int seatsRemaining = MAX_CAPACITY - currentCapacity;
            if (tableSize > seatsRemaining) {
                response.status(400);
                response.write("Booking denied. Only " + seatsRemaining + " seats remaining for this time slot.");
                return;
            }

            String bookingId = UUID.randomUUID().toString();
            booking.setUuid(bookingId);
            bookings.put(bookingId, booking);

            response.status(200);
            response.contentType("text/plain");
            response.write("Booking Confirmed! ID: " + bookingId +
                    "\nName: " + customerName +
                    "\nDate: " + date +
                    "\nTime: " + time +
                    "\nTable Size: " + tableSize);
        }
    }

    public static class GetAllBookingsHandler implements RouteHandler {
        @Override
        public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {
            Map<String, Map<String, List<Booking>>> groupedBookings = new HashMap<>();

            for (Booking booking : bookings.values()) {
                groupedBookings
                        .computeIfAbsent(booking.getDate(), k -> new HashMap<>())
                        .computeIfAbsent(booking.getTime(), k -> new ArrayList<>())
                        .add(booking);
            }

            response.contentType("application/json");
            response.write(new Gson().toJson(groupedBookings));
        }
    }
}
