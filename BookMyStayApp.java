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
        String roomId;
        boolean isCancelled = false;

        Reservation(String reservationId, String guestName, String roomType, String roomId) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
        }
    }

    static class RoomInventory {

        HashMap<String, Integer> inventory = new HashMap<>();

        RoomInventory() {
            inventory.put("Single", 1);
            inventory.put("Double", 1);
            inventory.put("Suite", 1);
        }

        void increment(String type) {
            inventory.put(type, inventory.getOrDefault(type, 0) + 1);
        }

        void display() {
            System.out.println("Current Inventory:");
            for (String type : inventory.keySet()) {
                System.out.println(type + ": " + inventory.get(type));
            }
            System.out.println();
        }
    }

    static class CancellationService {

        Stack<String> rollbackStack = new Stack<>();
        HashMap<String, Reservation> reservationMap;

        CancellationService(HashMap<String, Reservation> reservationMap) {
            this.reservationMap = reservationMap;
        }

        void cancel(String reservationId, RoomInventory inventory) {

            if (!reservationMap.containsKey(reservationId)) {
                System.out.println("Cancellation Failed: Reservation not found");
                return;
            }

            Reservation r = reservationMap.get(reservationId);

            if (r.isCancelled) {
                System.out.println("Cancellation Failed: Already cancelled");
                return;
            }

            rollbackStack.push(r.roomId);
            inventory.increment(r.roomType);
            r.isCancelled = true;

            System.out.println("Cancellation Successful for " + r.guestName + " (Room ID: " + r.roomId + ")");
        }

        void showRollbackStack() {
            System.out.println("Rollback Stack (Recently Released Room IDs): " + rollbackStack);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        HashMap<String, Reservation> reservationMap = new HashMap<>();

        Reservation r1 = new Reservation("R101", "Alice", "Single", "S1");
        Reservation r2 = new Reservation("R102", "Bob", "Double", "D1");

        reservationMap.put(r1.reservationId, r1);
        reservationMap.put(r2.reservationId, r2);

        CancellationService service = new CancellationService(reservationMap);

        inventory.display();

        service.cancel("R101", inventory);
        service.cancel("R999", inventory); // invalid
        service.cancel("R101", inventory); // already cancelled

        inventory.display();
        service.showRollbackStack();
    }
}