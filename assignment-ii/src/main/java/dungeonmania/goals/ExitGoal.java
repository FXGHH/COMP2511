package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class ExitGoal implements Goal{
    private String type;
    private int target;

    public ExitGoal(String type) {
        this.type = type;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
                Player character = game.getPlayer();
                Position pos = character.getPosition();
                List<Exit> es = game.getMap().getEntities(Exit.class);
                if (es == null || es.size() == 0) return false;
                return es
                    .stream()
                    .map(Entity::getPosition)
                    .anyMatch(pos::equals);
    }

    public String toString(Game game) {
        if (!achieved(game)) {
            return ":exit";
        } 
        return "";
    }

    public String getType() {
        return type;
    }

    public int getTarget() {
        return target;
    }
}
