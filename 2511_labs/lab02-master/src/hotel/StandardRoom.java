package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class StandardRoom extends Room {

    public Booking book(LocalDate arrival, LocalDate departure) {
        return super.roomBook(arrival, departure);
    }

    
    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
}