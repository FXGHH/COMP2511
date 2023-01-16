package dungeonmania.entities.enemies.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public interface MoveAction{
    public void moveStrategy(Game game, Position p, Entity e);
}
