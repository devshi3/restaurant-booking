package app.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import app.model.Booking;

public class BookingRepository {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    public void save(String id, Booking booking) {
        bookings.put(id, booking);
    }

    public Collection<Booking> getAll() {
        return bookings.values();
    }

    public int getCapacityForSlot(String date, String time) {
        return bookings.values().stream()
            .filter(b -> b.getDate().equals(date) && b.getTime().equals(time))
            .mapToInt(Booking::getTableSize)
            .sum();
    }
}
