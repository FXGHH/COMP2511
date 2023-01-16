package dungeonmania.entities;

import java.util.Random;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Swamp extends Entity{
    private int movementFactor;

    public Swamp(Position position, String type) {
        super(position, type);
        SetMovementFactor();
    }
    

    public void SetMovementFactor(){
        Random rand = new Random();
        movementFactor = rand.nextInt(10);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public void SetMovementFactor(int time){
        movementFactor = time;
    }

    public int getMovementFactor(){
        return movementFactor;
    }

    static public boolean hasSwampTile(GameMap map, Position position) {
        return (map.getEntities(position).stream().anyMatch(e -> e.getType().equals("swamp_tile")));
    }

    static public Swamp getSwamp(GameMap map, Position position) {
        for (Entity e : map.getEntities(position)) {
            if (e.getType().equals("swamp_tile")) {
                return (Swamp)e;
            }
        }
        return null;
    }
}
