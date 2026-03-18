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

    static class InvalidBookingException extends Exception {
        InvalidBookingException(String message) {
            super(message);
        }
    }

    static class RoomInventory {

        HashMap<String, Integer> inventory = new HashMap<>();

        RoomInventory() {
            inventory.put("Single", 2);
            inventory.put("Double", 1);
            inventory.put("Suite", 0);
        }

        int getAvailability(String type) {
            return inventory.getOrDefault(type, -1);
        }

        void reduceAvailability(String type) throws InvalidBookingException {

            if (!inventory.containsKey(type)) {
                throw new InvalidBookingException("Invalid room type: " + type);
            }

            int count = inventory.get(type);

            if (count <= 0) {
                throw new InvalidBookingException("No rooms available for: " + type);
            }

            inventory.put(type, count - 1);
        }
    }

    static class BookingValidator {

        static void validate(Reservation r, RoomInventory inventory) throws InvalidBookingException {

            if (r.roomType == null || r.roomType.isEmpty()) {
                throw new InvalidBookingException("Room type cannot be empty");
            }

            if (inventory.getAvailability(r.roomType) == -1) {
                throw new InvalidBookingException("Room type does not exist: " + r.roomType);
            }

            if (inventory.getAvailability(r.roomType) <= 0) {
                throw new InvalidBookingException("Room not available: " + r.roomType);
            }
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite"); // unavailable
        Reservation r3 = new Reservation("R103", "Charlie", "Deluxe"); // invalid

        List<Reservation> requests = Arrays.asList(r1, r2, r3);

        for (Reservation r : requests) {

            try {
                BookingValidator.validate(r, inventory);
                inventory.reduceAvailability(r.roomType);

                System.out.println("Booking Confirmed for " + r.guestName + " (" + r.roomType + ")");

            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed for " + r.guestName + ": " + e.getMessage());
            }
        }
    }
}