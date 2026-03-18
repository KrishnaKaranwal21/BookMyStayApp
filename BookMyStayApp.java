import java.util.*;

public class BookMyStayApp {

    static abstract class Room {
        int beds;
        int size;
        double price;

        Room(int beds, int size, double price) {
            this.beds = beds;
            this.size = size;
            this.price = price;
        }
    }

    static class Reservation {
        String reservationId;
        String guestName;
        String roomType;

        Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class BookingHistory {

        List<Reservation> history = new ArrayList<>();

        void addReservation(Reservation r) {
            history.add(r);
        }

        List<Reservation> getAllReservations() {
            return history;
        }
    }

    static class BookingReportService {

        void displayAllBookings(List<Reservation> reservations) {

            System.out.println("Booking History Report");
            System.out.println("--------------------------------");

            for (Reservation r : reservations) {
                System.out.println("Reservation ID: " + r.reservationId);
                System.out.println("Guest Name: " + r.guestName);
                System.out.println("Room Type: " + r.roomType);
                System.out.println("--------------------------------");
            }
        }

        void displaySummary(List<Reservation> reservations) {

            HashMap<String, Integer> summary = new HashMap<>();

            for (Reservation r : reservations) {
                summary.put(r.roomType, summary.getOrDefault(r.roomType, 0) + 1);
            }

            System.out.println("Booking Summary");
            System.out.println("--------------------------------");

            for (String type : summary.keySet()) {
                System.out.println(type + " Rooms Booked: " + summary.get(type));
            }
        }
    }

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Double");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        BookingReportService reportService = new BookingReportService();

        reportService.displayAllBookings(history.getAllReservations());
        reportService.displaySummary(history.getAllReservations());
    }
}