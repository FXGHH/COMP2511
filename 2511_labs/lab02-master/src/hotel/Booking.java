package hotel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.junit.After;
import org.junit.runner.notification.RunListener.ThreadSafe;

public class Booking{
    
    // public static Object toJSON;
    LocalDate arrival;
    LocalDate departure;

    public Booking(LocalDate arrival, LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    /**
     * @return a JSONObject of the form {"arrival": arrival, "departure": departure}
     */
    public JSONObject toJSON() {
        JSONObject booking = new JSONObject();
        booking.put("arrival", arrival.toString());
        booking.put("departure", departure.toString());
        return booking;
    }

    /**
     * Checks whether two dates overlap
     * @param start
     * @param end
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        if ((this.arrival.isBefore(start) && this.departure.isAfter(start))) {
            return true;
        } else if (this.arrival.isAfter(start) && this.departure.isBefore(end)) {
            return true;
        } else if (this.arrival.isEqual(start) || (this.departure.isEqual(end))) {
            return true;
        } else if (this.arrival.isAfter(start) && this.departure.isAfter(end)) {
            return true;
        } else {
            return false;

        }
    }
    public static void main(String[] args) {
        LocalDate bookingStart = LocalDate.of(2021, 4, 21);
        LocalDate bookingEnd = LocalDate.of(2020, 4, 21);
        System.out.println(bookingStart.isBefore(bookingEnd));
    }

}