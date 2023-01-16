package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class Boulders implements Goal{
    private String type;
    private int target;
    // private Goal goal1;
    // private Goal goal2;

    public Boulders(String type) {
        this.type = type;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    public String toString(Game game) {
        if (!achieved(game)) {
            return ":boulders";
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