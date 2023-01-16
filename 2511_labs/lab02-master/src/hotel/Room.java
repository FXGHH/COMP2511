package hotel;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sound.midi.Soundbank;

public abstract class Room{
    /**
     * Checks if the room is not booked out during the given time.
     * If so, creates a booking for the room at that time.
     * @param arrival
     * @param departure
     * @return The booking object if the booking succeeded, null if failed
     */
    public List<Booking> bookings = new ArrayList<Booking>();
    String type;
    
    public Booking roomBook(LocalDate arrival, LocalDate departure) {
        for (Booking booking : bookings) {
            if (booking.overlaps(arrival, departure)) {
                System.out.println("overlaped");
                return null;
            }
        }
        Booking booking = new Booking(arrival, departure);
        bookings.add(booking);
        return booking;
    };

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ],
     *  "type": the type of the room (standard, ensuite, penthouse)
     * }
     */
    // public abstract JSONObject toJSON();
    public JSONObject toJSON() {
        JSONObject json_obj = new JSONObject();
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < bookings.size(); i++) {
            newArray.put(bookings.get(i).toJSON());
        }
        json_obj.put("type", type);
        json_obj.put("bookings", newArray);
        return json_obj;
    };

    /**
     * Prints a welcome message to the guest staying in the room.
     */
    public abstract void printWelcomeMessage();

}