package dungeonmania.entities.enemies.movement;

import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMove implements MoveAction{

    @Override
    public void moveStrategy(Game game, Position position, Entity e) {
        Position nextPos;
        GameMap map = game.getMap();
        Random randGen = new Random();
            List<Position> pos = position.getCardinallyAdjacentPositions();
            pos = pos
                .stream()
                .filter(p -> map.canMoveTo(e, p)).collect(Collectors.toList());
            if (pos.size() == 0) {
                nextPos = position;
                map.moveTo(e, nextPos);
            } else {
                nextPos = pos.get(randGen.nextInt(pos.size()));
                map.moveTo(e, nextPos);
            }
    }
    
}
