package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Sword;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();
    // private List<String> validBuilds = getalidBuilds();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {
        List<String> canBuild = new ArrayList<>();
        if (Bow.checkCanBuild() != null) canBuild.add("bow");
        if (Shield.checkCanBuild() != null) canBuild.add("shield");
        if (Sceptre.checkCanBuild() != null) canBuild.add("sceptre");
        if (MidnightArmour.checkCanBuild() != null) canBuild.add("midnight_armour");
        return canBuild;
    }


    public InventoryItem checkBuildCriteria(Player p, String entity, EntityFactory factory) {
        switch(entity) {
            case("bow"):
                removeIngredients(Bow.getIngredients());
                return new Bow();

            case("shield"):
                removeIngredients(Shield.getIngredients());
                return new Shield();
            case("sceptre"):
                removeIngredients(Sceptre.getIngredients());
                return new Sceptre();

            case("midnight_armour"):
                removeIngredients(MidnightArmour.getIngredients());
                return new MidnightArmour();

            default:
                throw new IllegalCallerException();
        }
    }
    
    public void removeIngredients(List<String> ingredients) {

        for (String ingredient : ingredients) {
            for (InventoryItem e : items) {
                if (e.getType().equals(ingredient)) {
                    items.remove(e);
                    break;
                }
            }
        }
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId)) return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public int getItemNum() {
        return items.size();
    }
}
