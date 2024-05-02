package ferry.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeTableService {

    private final TimeTables timeTables;
    public final Bookings bookings;
    private final FerryAvailabilityService ferryService;

    public TimeTableService(TimeTables timeTables, Bookings bookings, FerryAvailabilityService ferryService) {
        this.timeTables = timeTables;
        this.bookings = bookings;
        this.ferryService = ferryService;
    }

    public List<TimeTableViewModelRow> getTimeTable(List<Port> ports) {
        List<TimeTable> timetables = timeTables.all();
        List<TimeTableEntry> allEntries = new ArrayList<>();
        for (TimeTable tt : timetables) {
            allEntries.addAll(tt.entries);
        }
        allEntries.sort(Comparator.comparingLong(tte -> tte.time));

        List<TimeTableViewModelRow> rows = new ArrayList<>();

        for (TimeTableEntry timetable : allEntries) {
            Port origin = null;
            Port destination = null;
            for (Port x : ports) {
                if (x.id == timetable.originId) {
                    origin = x;
                }
                if (x.id == timetable.destinationId) {
                    destination = x;
                }
            }
            String format = "%02d:%02d";
            String destinationName = destination.name;
            String originName = origin.name;
            Ferry ferry = ferryService.nextFerryAvailableFrom(origin.id, timetable.time);
            long arrivalTime = timetable.time + timetable.journeyTime;
            TimeTableViewModelRow row = new TimeTableViewModelRow();
            row.destinationPort = destinationName;
            row.ferryName = ferry == null ? "" : ferry.name;
            row.journeyLength = String.format(format, timetable.journeyTime / 60, timetable.journeyTime % 60);
            row.originPort = originName;
            row.startTime = String.format(format, timetable.time / 60, timetable.time % 60);
            row.arrivalTime = String.format(format, arrivalTime / 60, arrivalTime % 60);
            rows.add(row);
        }
        return rows;
    }

    public List<AvailableCrossing> getAvailableCrossings(long time, int fromPort, int toPort) {
        List<Port> ports = new Ports().all();
        List<TimeTableEntry> allEntries = getAllTimeTableEntries();

        List<AvailableCrossing> result = new ArrayList<>();

        for (TimeTableEntry timetable : allEntries) {
            Port origin = findPort(ports, timetable.originId);
            Port destination = findPort(ports, timetable.destinationId);

            if (toPort == destination.id && fromPort == origin.id && timetable.time >= time) {
                Ferry ferry = ferryService.nextFerryAvailableFrom(timetable.originId, timetable.time);
                int seatsLeft = ferry.passengers - calculatePassengers(timetable.id);

                if (seatsLeft > 0) {
                    result.add(createAvailableCrossing(timetable, origin, destination, ferry, seatsLeft));
                }
            }
        }
        return result;
    }
}