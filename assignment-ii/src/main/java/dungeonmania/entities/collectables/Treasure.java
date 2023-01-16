package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Treasure extends Entity implements InventoryItem {
    public Treasure(Position position, String nameOfEntity) {
        super(position, nameOfEntity);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public boolean onOverlap(GameMap map, Entity entity) {
        return true;
    }
}
