package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PenthouseRoom extends Room {

    // @Override
    public Booking book(LocalDate arrival, LocalDate departure) {
        return super.roomBook(arrival, departure);
    }


    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your penthouse apartment, complete with ensuite, lounge, kitchen and master bedroom.");
    }
    
}