package bool;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import bool.BooleanNode;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        // Return the expression evaluated
        return expression.evaluate();
        // BooleanNode b = new BooleanNode();
    }

    public static String prettyPrint(BooleanNode expression) {
        // Pretty print the expression
        return expression.getString();
    }

    public static void main(String[] args) throws IOException {
        Leaf newLeafTrue = new Leaf(true);
        Leaf newLeafFalse = new Leaf(false);
        Leaf newLeafFalse2 = new Leaf(false);

        AndComposite newComposite1 = new AndComposite();
        OrComposite newComposite2 = new OrComposite();
        NotComposite newComposite3 = new NotComposite();
        OrComposite newCompositeAll = new OrComposite();

        newCompositeAll.add(newLeafTrue);
        newCompositeAll.add(newComposite3);

        // not below
        newComposite3.add(newComposite1);

        // and bellow 
        newComposite1.add(newLeafFalse2);
        newComposite1.add(newComposite2);

        // or below
        newComposite2.add(newLeafTrue);
        newComposite2.add(newLeafFalse);

        Boolean result = BooleanEvaluator.evaluate(newCompositeAll);
        String prettyResult = BooleanEvaluator.prettyPrint(newCompositeAll);

        System.out.println(prettyResult + " = " + result);
        
        try {
            String jstxt = Files.readString(Path.of("C:/Users/Think/COMP2511/lab07/src/main/bool/Node.json"));
            JSONObject json = new JSONObject(jstxt);

            System.out.println(prettyPrint(NodeFactory.factoryBuild(json)));
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}