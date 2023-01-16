package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Exit extends Entity {
    public Exit(Position position, String nameOfEntity) {
        super(position.asLayer(Entity.ITEM_LAYER), nameOfEntity);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
