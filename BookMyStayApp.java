import java.io.*;
import java.util.*;

public class BookMyStayApp {

    static abstract class Room implements Serializable {
        int beds;
        int size;
        double price;

        Room(int beds, int size, double price) {
            this.beds = beds;
            this.size = size;
            this.price = price;
        }
    }

    static class Reservation implements Serializable {
        String reservationId;
        String guestName;
        String roomType;

        Reservation(String id, String name, String type) {
            this.reservationId = id;
            this.guestName = name;
            this.roomType = type;
        }
    }

    static class SystemState implements Serializable {
        HashMap<String, Integer> inventory;
        List<Reservation> bookings;

        SystemState(HashMap<String, Integer> inventory, List<Reservation> bookings) {
            this.inventory = inventory;
            this.bookings = bookings;
        }
    }

    static class PersistenceService {

        private static final String FILE_NAME = "hotel_state.dat";

        void save(SystemState state) {

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(state);
                System.out.println("System state saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving system state: " + e.getMessage());
            }
        }

        SystemState load() {

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                SystemState state = (SystemState) ois.readObject();
                System.out.println("System state loaded successfully.");
                return state;
            } catch (Exception e) {
                System.out.println("No previous state found. Starting fresh.");
                return null;
            }
        }
    }

    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        SystemState state = service.load();

        HashMap<String, Integer> inventory;
        List<Reservation> bookings;

        if (state == null) {

            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 1);

            bookings = new ArrayList<>();
            bookings.add(new Reservation("R1", "Alice", "Single"));

            System.out.println("Initialized new system state.");

        } else {

            inventory = state.inventory;
            bookings = state.bookings;

            System.out.println("Recovered Inventory: " + inventory);
            System.out.println("Recovered Bookings: " + bookings.size());
        }

        service.save(new SystemState(inventory, bookings));
    }
}