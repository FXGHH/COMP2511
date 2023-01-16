package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem{

    public Buildable(Position position ,String type) {
        super(position, type);
    }

    // Check if we have enough item
    static public boolean hasEnoughitems(List<String> ingredients) {
        List<String> currInventoryLs = getAllEntities().stream().map(e -> e.getType()).collect(Collectors.toList());
        ArrayList<String> currInventory = new ArrayList<String>(currInventoryLs);
        for (String ingredient : ingredients) {
            if (currInventory.contains(ingredient)) {
                currInventory.remove(ingredient);
            } else {
                return false;
            }
        }
        return true;
    }

    // replace all key item to sun stone item from the given ingredients
    static public List<String> sunStoneReplacement(List<List<String>> ingredients) {
        for (List<String> ingredient : ingredients) {
            if (ingredient.contains("key")) {
                List<String> newIngredients = new ArrayList<>(ingredient);
                newIngredients.remove("key");
                newIngredients.add("sun_stone");
                if (hasEnoughitems(newIngredients)) {
                    newIngredients.remove("sun_stone");
                    return newIngredients;
                }
            }
        }
        return null;
    }

    static public List<String> getBuildables() {
        return List.of("bow", "shield", "midnight_armour", "sceptre");
    }
}
