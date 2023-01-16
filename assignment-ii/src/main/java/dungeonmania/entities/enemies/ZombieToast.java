package dungeonmania.entities.enemies;

import java.util.Random;
import dungeonmania.Game;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.enemies.movement.MoveAction;
import dungeonmania.entities.enemies.movement.RandomMove;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    private Random randGen = new Random();
    private MoveAction moveAction;

    public ZombieToast(Position position, String nameOfEntity) {
        super(position, getHealth(), getAttack(), nameOfEntity);
        // super(position, getHealth(), getAttack(), "zombie_toast");
    }

    @Override
    public void move(Game game) {
        moveAction = new RandomMove();
        moveAction.moveStrategy(game, getPosition(), this);
    }

    public Random getRandGen() {
        return randGen;
    }

    public static double getAttack() {
        return EntityFactory.gteCofigs().optDouble("zombie_attack");
    }

    public static double getHealth() {
        return EntityFactory.gteCofigs().optDouble("zombie_health");
    }
}
