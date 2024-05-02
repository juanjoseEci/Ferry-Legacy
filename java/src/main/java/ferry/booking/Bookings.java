package ferry.booking;

import java.util.ArrayList;
import java.util.List;

public class Bookings {

    public final List<Booking> booking = new ArrayList<>();

    public void add(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> all() {
        return bookings;
    }
}
