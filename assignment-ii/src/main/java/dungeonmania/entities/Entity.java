package dungeonmania.entities;

import dungeonmania.DungeonManiaController;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.UUID;

public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;
    private String type;
    private int dwellTime = 0;
    private boolean needDwell = false;
    // private HashMap<String, Integer> attribute;

    public Entity(Position position, String type) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
        this.type = type;
    }


    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Direction direction) {
        previousPosition = this.position;
        this.position = Position.translateBy(this.position, direction);
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Position offset) {
        this.position = Position.translateBy(this.position, offset);
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    public boolean onOverlap(GameMap map, Entity entity) {
        return false;
    }

    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    public void onDestroy(GameMap gameMap) {
        return;
    }

    public Position getPosition() {
        return position;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public String getType(){
        return this.type;
    }

    // Get entities from player have
    static public List<Entity> getAllEntities() {
        return DungeonManiaController.getCurrGame().getEntities();
    }

    // Get entities from game map
    static public List<Entity> getMapEntities() {
        return DungeonManiaController.getCurrGame().getMapEntity();
    }
    public boolean checkNeedDwell(GameMap map) {
        if (dwellTime > 0) {
            dwellTime--;
            return true;
        }
        if (needDwell == false) {
            if (Swamp.hasSwampTile(map, position)) {
                this.dwellTime = Swamp.getSwamp(map, position).getMovementFactor() - 1;
                // Check if the movement factor is 0, then the entity don't need dwell
                if (this.dwellTime < 0) {
                    return false;
                } else {
                    needDwell = true;
                    return true;
                }
            }
            return false;
        }
        if (needDwell == true) {
            needDwell = false;
            this.dwellTime = 0;
            return false;
        }
        return false;
    }

    static public Entity findEntityWithPosition(Position position) {
        List<Entity> allEntities = getMapEntities();
        Entity AimEntity = null;
        for (Entity e : allEntities) {
            if (e.position.equals(position) && !(e instanceof Enemy)) {
                AimEntity = e;
            }
            
        }
        return AimEntity;
    }

    static public Player getPlayer() {
        return DungeonManiaController.getCurrGame().getPlayer();
    }

    // public HashMap<String, String> getEntityAttribute() {
    //     return null;
    // }
    // public void setAttribute(HashMap<String, String> attributes) {
    // }
}
