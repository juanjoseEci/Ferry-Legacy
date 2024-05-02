package ferry.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JourneyBookingService {

    private TimeTables timeTables;
    private Bookings bookings;
    private final FerryAvailabilityService ferryService;

    public JourneyBookingService(TimeTables timeTables, Bookings bookings, FerryAvailabilityService ferryService) {
        this.timeTables = timeTables;
        this.bookings = bookings;
        this.ferryService = ferryService;
    }

    public boolean canBook(int journeyId, int passengers) {
        List<TimeTable> timetables = timeTables.all();
        List<TimeTableEntry> allEntries = new ArrayList<>();
        for (TimeTable tt : timetables) {
            allEntries.addAll(tt.entries);
        }
        Collections.sort(allEntries, (tte1, tte2) -> Long.compare(tte1.time, tte2.time));
        for (TimeTableEntry timetable : allEntries) {
            Ferry ferry = ferryService.nextFerryAvailableFrom(timetable.originId, timetable.time);

            if (timetable.id == journeyId) {
                List<Booking> journeyBookings = new ArrayList<>();
                for (Booking x : bookings.all()) {
                    if (x.journeyId == journeyId) {
                        journeyBookings.add(x);
                    }
                }
                int pax = 0;
                for (Booking x : bookings.all()) {
                    pax += x.passengers;
                }
                int seatsLeft = ferry.passengers - pax;
                return seatsLeft >= passengers;
            }
        }
        return false;
    }

    public void book(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getAllBookings() {
        return bookings.all();
    }
}