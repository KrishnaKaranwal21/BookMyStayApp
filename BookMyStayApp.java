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

        Reservation(String id, String name, String type) {
            this.reservationId = id;
            this.guestName = name;
            this.roomType = type;
        }
    }

    static class RoomInventory {

        private HashMap<String, Integer> inventory = new HashMap<>();

        RoomInventory() {
            inventory.put("Single", 2);
        }

        synchronized boolean allocateRoom(String type) {

            int count = inventory.getOrDefault(type, 0);

            if (count > 0) {
                inventory.put(type, count - 1);
                return true;
            }

            return false;
        }

        void display() {
            System.out.println("Final Inventory: " + inventory);
        }
    }

    static class BookingQueue {

        private Queue<Reservation> queue = new LinkedList<>();

        synchronized void addRequest(Reservation r) {
            queue.offer(r);
        }

        synchronized Reservation getRequest() {
            return queue.poll();
        }
    }

    static class BookingProcessor extends Thread {

        private BookingQueue queue;
        private RoomInventory inventory;

        BookingProcessor(BookingQueue q, RoomInventory i) {
            this.queue = q;
            this.inventory = i;
        }

        public void run() {

            while (true) {

                Reservation r;

                synchronized (queue) {
                    r = queue.getRequest();
                }

                if (r == null) break;

                boolean success;

                synchronized (inventory) {
                    success = inventory.allocateRoom(r.roomType);
                }

                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " CONFIRMED booking for " + r.guestName);
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " FAILED booking for " + r.guestName);
                }
            }
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("R1", "Alice", "Single"));
        queue.addRequest(new Reservation("R2", "Bob", "Single"));
        queue.addRequest(new Reservation("R3", "Charlie", "Single"));
        queue.addRequest(new Reservation("R4", "David", "Single"));

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.display();
    }
}