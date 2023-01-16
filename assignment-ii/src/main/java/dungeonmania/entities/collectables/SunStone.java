package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SunStone extends Entity implements InventoryItem{
    public SunStone(Position position, String nameOfEntity) {
        super(position, nameOfEntity);
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
}
