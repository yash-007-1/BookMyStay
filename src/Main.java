import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

class InventoryService {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

class BookingService {

    private InventoryService inventory;

    private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

    private Set<String> allRoomIds = new HashSet<>();

    private int roomCounter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processReservation(Reservation r) {

        String roomType = r.getRoomType();

        if (inventory.getAvailability(roomType) > 0) {

            String roomId = roomType.replace(" ", "") + "-" + roomCounter++;

            while (allRoomIds.contains(roomId)) {
                roomId = roomType.replace(" ", "") + "-" + roomCounter++;
            }

            allRoomIds.add(roomId);

            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            inventory.decrementRoom(roomType);

            System.out.println("Reservation Confirmed");
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + roomType);
            System.out.println("Room ID: " + roomId);
            System.out.println();

        } else {

            System.out.println("Reservation Failed for " + r.getGuestName());
            System.out.println("No rooms available for " + roomType);
            System.out.println();
        }
    }
}

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.addRequest(new Reservation("Arjun", "Single Room"));
        requestQueue.addRequest(new Reservation("Priya", "Double Room"));
        requestQueue.addRequest(new Reservation("Rahul", "Suite Room"));
        requestQueue.addRequest(new Reservation("Neha", "Single Room"));

        InventoryService inventory = new InventoryService();

        BookingService bookingService = new BookingService(inventory);

        while (requestQueue.hasRequests()) {

            Reservation r = requestQueue.getNextRequest();

            bookingService.processReservation(r);
        }
    }
}