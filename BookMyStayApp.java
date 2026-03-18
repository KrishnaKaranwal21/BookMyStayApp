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

    static class AddOnService {
        String name;
        double cost;

        AddOnService(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }
    }

    static class AddOnServiceManager {

        HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

        void addService(String reservationId, AddOnService service) {
            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);
        }

        void displayServices(String reservationId) {
            List<AddOnService> services = serviceMap.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No add-on services selected.");
                return;
            }

            double total = 0;

            System.out.println("Services for Reservation ID: " + reservationId);

            for (AddOnService s : services) {
                System.out.println(s.name + " - " + s.cost);
                total += s.cost;
            }

            System.out.println("Total Add-On Cost: " + total);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Reservation r1 = new Reservation("R101", "Alice", "Single");

        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa", 1000);

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(r1.reservationId, wifi);
        manager.addService(r1.reservationId, breakfast);
        manager.addService(r1.reservationId, spa);

        manager.displayServices(r1.reservationId);
    }
}