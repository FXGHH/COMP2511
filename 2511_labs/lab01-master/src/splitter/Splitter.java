package splitter;
import java.util.Scanner;

public class Splitter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a sentence specified by spaces only: ");
        String string = scanner.nextLine();
        for (String retval: string.split(" ")) {
            System.out.println(retval);
        }
        scanner.close();
    }
}
