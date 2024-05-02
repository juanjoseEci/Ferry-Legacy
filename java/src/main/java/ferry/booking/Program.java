package ferry.booking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Program {

    private static final Logger LOGGER = Logger.getLogger(Program.class.getName());

    private static TimeTableService timeTableService;
    private static JourneyBookingService bookingService;
    private static Ports ports;
    public static final FerryAvailabilityService ferryService;

    public static void wireUp() {
        TimeTables timeTables = new TimeTables();
        Ferries ferries = new Ferries();
        Bookings bookings = new Bookings();
        ports = new Ports();
        FerryAvailabilityService ferryService = new FerryAvailabilityService(timeTables, new PortManager(ports, ferries));
        bookingService = new JourneyBookingService(timeTables, bookings, ferryService);
        timeTableService = new TimeTableService(timeTables, bookings, ferryService);
    }

    public static void main(String[] args) {
        start();
        commandLoop();
    }

    public static void start() {
        wireUp();

        LOGGER.info("Welcome to the Ferry Finding System");
        LOGGER.info("=======");
        LOGGER.info("Ferry Time Table");

        List<Port> allPorts = ports.all();
        List<TimeTableViewModelRow> timeTable = timeTableService.getTimeTable(allPorts);

        displayTimetable(allPorts, timeTable);
    }

    public static void displayTimetable(List<Port> ports, List<TimeTableViewModelRow> rows) {
        for (Port port : ports) {
            printPortHeader(port.name);
            List<TimeTableViewModelRow> items = new ArrayList<>();
            for (TimeTableViewModelRow x : rows) {
                if (x.originPort.equals(port.name)) {
                    items.add(x);
                }
            }
            items.sort(Comparator.comparing(tt -> tt.startTime));

            for (TimeTableViewModelRow item : items) {
                LOGGER.info(String.format("| %-8s | %-13s | %-13s | %-18s | %-8s |", item.startTime, item.destinationPort,
                        item.journeyLength, item.ferryName, item.arrivalTime));
            }
        }
    }

    private static void commandLoop() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            line = br.readLine();
            while (!"quit".equals(line)) {
                doCommand(line);

                line = (br.readLine()).toLowerCase();
            }
        } catch (IOException e) {
            LOGGER.severe("Error reading command: " + e.getMessage());
        }
    }

    private static void doCommand(String command) {
        if (command.startsWith("search")) {
            search(command);
        } else if (command.startsWith("book")) {
            book(command);
        } else if (command.startsWith("list ports")) {
            LOGGER.info("Ports:");
            LOGGER.info("------");
            for (Port port : ports.all()) {
                LOGGER.info(String.format("%d - %s", port.id, port.name));
            }
            LOGGER.info("");
        } else if (command.startsWith("list bookings")) {
            List<Booking> bookings = bookingService.getAllBookings();
            LOGGER.info("Bookings:");
            LOGGER.info("---------");
            for (Booking b : bookings) {
                LOGGER.info(String.format("journey %d - passengers %d", b.journeyId, b.passengers));
            }
            LOGGER.info("");
        } else {
            LOGGER.info("Commands are: [search x y hh:mm] book, or list bookings");
            LOGGER.info("  search x y hh:mm");
            LOGGER.info("  book x y");
            LOGGER.info("  list bookings");
            LOGGER.info("  list ports");
            LOGGER.info("");
            LOGGER.info("Book is [book x y]");
            LOGGER.info("where x - journey id");
            LOGGER.info("where y - number of passenger");
            LOGGER.info("");
            LOGGER.info("Search is [search x y hh:mm]");
            LOGGER.info("where: x - origin port id");
            LOGGER.info("where: y - destinationg port id");
            LOGGER.info("where: hh:mm - time to search after");
        }
    }

    private static void book(String line) {
        try {
            String[] parts = line.split(" ");
            int journeyId = Integer.parseInt(parts[1]);
            int passengers = Integer.parseInt(parts[2]);

            if (bookingService.canBook(journeyId, passengers)) {
                Booking booking = new Booking();
                booking.journeyId = journeyId;
                booking.passengers = passengers;
                bookingService.book(booking);

                LOGGER.info("Booked");
            } else {
                LOGGER.info("Cannot book that journey");
            }
        } catch (Exception e) {
            LOGGER.info("Book is [book x y]");
            LOGGER.info("where x - journey id");
            LOGGER.info("where y - number of passenger");
        }
    }

    private static void search(String line) {
        try {
            String[] parts = line.split(" ");
            int originPortId = Integer.parseInt(parts[1]);
            int destinationPortId = Integer.parseInt(parts[2]);
            String[] mins = parts[3].split(":");
            long time = Long.parseLong(mins[0]) * 60 + Long.parseLong(mins[1]);

            List<AvailableCrossing> search = timeTableService.getAvailableCrossings(time, originPortId,
                    destinationPortId);

            for (AvailableCrossing result : search) {
                LOGGER.info(String.format("[%02d:%02d] %s to %s -  %s (JourneyId : %d, spaces left %d)", result.setOff / 60,
                        result.setOff % 60, result.originPort, result.destinationPort, result.ferryName,
                        result.journeyId, result.seatsLeft));
            }
        } catch (Exception e) {
            LOGGER.info("Search is [search x y hh:mm]");
            LOGGER.info("where: x - origin port id");
            LOGGER.info("where: y - destinationg port id");
            LOGGER.info("where: hh:mm - time to search after");
        }
    }
    private static void printPortHeader(String portName) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nDepartures from {0}\n", portName);
            LOGGER.info("\n --------------------------------------------------------------------------");
            LOGGER.info("| %-8s | %-13s | %-13s | %-18s | %-8s |", "Time", "Destination", "Journey Time", "Ferry", "Arrives");
            LOGGER.info(" --------------------------------------------------------------------------");
        }
    }
}