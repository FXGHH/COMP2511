package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class EnsuiteRoom extends Room {

    public Booking book(LocalDate arrival, LocalDate departure) {
        return super.roomBook(arrival, departure);
    }


    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your beautiful ensuite room which overlooks the Sydney harbour. Enjoy your stay");
    }
    
}