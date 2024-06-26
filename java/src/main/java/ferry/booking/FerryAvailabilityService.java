package ferry.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FerryAvailabilityService {

    private final TimeTables timeTables;
    private final PortManager portManager;

    public FerryAvailabilityService(TimeTables timeTables, PortManager portManager) {
        this.timeTables = timeTables;
        this.portManager = portManager;
    }

    public Ferry nextFerryAvailableFrom(int portId, long time) {
        List<PortModel> ports = portManager.PortModels();
        List<TimeTableEntry> allEntries = new ArrayList<>();
        for (TimeTable tt : timeTables.all()) {
            allEntries.addAll(tt.entries);
        }
        Collections.sort(allEntries, (tte1, tte2) -> Long.compare(tte1.time, tte2.time));

        for (TimeTableEntry entry : allEntries) {
            FerryJourney ferry = FerryManager.createFerryJourney(ports, entry);
            if (ferry != null) {
                boatReady(entry, ferry.destination, ferry);
            }
            if (entry.originId == portId && entry.time >= time && ferry != null) {
                return ferry.ferry;
            }
        }

        return null;
    }

    private static void boatReady(TimeTableEntry timetable, PortModel destination, FerryJourney ferryJourney) {
        if (ferryJourney.ferry == null) {
            FerryManager.addFerry(timetable, ferryJourney);
        }
        Ferry ferry = ferryJourney.ferry;

        long time = FerryModule.timeReady(timetable, destination);
        destination.addBoat(time, ferry);
    }
}