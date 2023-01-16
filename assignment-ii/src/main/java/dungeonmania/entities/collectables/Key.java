package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends Entity implements InventoryItem {
    private int number;

    public Key(Position position, int number, String nameOfEntity) {
        super(position, nameOfEntity);
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public boolean onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            return true;
        }
        return false;
    }

    public int getnumber() {
        return number;
    }
}
