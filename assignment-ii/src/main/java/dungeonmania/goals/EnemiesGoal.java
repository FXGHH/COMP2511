package dungeonmania.goals;


import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements Goal{

    private String type;
    private int target;

    public EnemiesGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }
    public boolean achieved(Game game) {
        return (game.battleCount() >= target) && (game.getMap().getEntities(ZombieToastSpawner.class).size() == 0);
    }

    public String toString(Game game) {
        if (!achieved(game)) {
            return ":enemies";
        } else {
            return "";
        }
    }

    public String getType() {
        return type;
    }

}