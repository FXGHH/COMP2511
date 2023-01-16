package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import hotel.Room;
import hotel.Booking;

public class Hotel{

    List<Room> rooms = new ArrayList<Room>();
    
    private String name;

    public Hotel(String name) {
        this.name = name;
    }

    /**
     * Makes a booking in any available room with the given preferences.
     * 
     * @param arrival
     * @param departure
     * @param standard - does the client want a standard room?
     * @param ensuite - does the client want an ensuite room?
     * @param penthouse - does the client want a penthouse room?
     * @return If there were no available rooms for the given preferences, returns false.
     * Returns true if the booking was successful
     */
    public boolean makeBooking(LocalDate arrival, LocalDate departure, boolean standard, boolean ensuite, boolean penthouse) {
        for (Room room : rooms) {
            if (roomDesired(room, standard, ensuite, penthouse) && room.roomBook(arrival, departure) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean roomDesired(Room room, boolean standard, boolean ensuite, boolean penthouse) {
        return ((room instanceof StandardRoom && standard) || (room instanceof EnsuiteRoom && ensuite) || (room instanceof PenthouseRoom && penthouse));
    }

    /**
     * @return A JSON Object of the form:
     * { "name": name, "rooms": [ each room as a JSON object, in order of creation ]}
     */
    public JSONObject toJSON() {
        JSONObject json_obj = new JSONObject();
        json_obj.put("name", this.name);

        JSONArray newArray = new JSONArray();
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).toJSON();
            if (rooms.get(i) instanceof StandardRoom) {
                rooms.get(i).type = "standard";
                newArray.put(rooms.get(i).toJSON());

            } else if (rooms.get(i) instanceof EnsuiteRoom) {
                rooms.get(i).type = "ensuite";
                newArray.put(rooms.get(i).toJSON());

            } else if (rooms.get(i) instanceof PenthouseRoom) {
                rooms.get(i).type = "penthouse";
                newArray.put(rooms.get(i).toJSON());
            }
        }
        json_obj.put("rooms", newArray);
        return json_obj;
    }

    public String getName() {
        return name;
    }

    public void addRoom(String roomType) {
        switch (roomType) {
            case "standard":
                rooms.add(new StandardRoom()); break;
            case "ensuite":
                rooms.add(new EnsuiteRoom()); break;
            case "penthouse":
                rooms.add(new PenthouseRoom()); break;
        } 
    }
}