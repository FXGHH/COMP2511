package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dungeonmania.entities.EntityFactory;

public class Sceptre extends Buildable{
    private int mindControlDuration;

    public Sceptre() {
        super(null, "scetre");
        this.mindControlDuration = getMindControlDurationSpec();
    }

    static public List<String> checkCanBuild() {
        ArrayList<List<String>> ingredients = new ArrayList<>();
        // add all possible method into the list 
        List<String> method1 = Arrays.asList("wood", "key", "sun_stone");
        ingredients.add(method1);
        List<String> method2 = Arrays.asList("wood", "treasure", "sun_stone");
        ingredients.add(method2);
        List<String> method3 = Arrays.asList("arrow", "arrow", "key", "sun_stone");
        ingredients.add(method3);
        List<String> method4 = Arrays.asList("arrow", "arrow", "treasure", "sun_stone");
        ingredients.add(method4);
        // check if we have enough ingredient
        for (List<String> method : ingredients) {
            if (hasEnoughitems(method)) {
                return method;
            }
        }
        return sunStoneReplacement(ingredients);
    }

    static public List<String> getIngredients() {
        return checkCanBuild();
    }

    public static int getMindControlDurationSpec() {
        return EntityFactory.gteCofigs().optInt("mind_control_duration");
    }

    public int getMindControlDuration() {
        return this.mindControlDuration;
    }

}
