package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class TreasureGoal implements Goal{

    private String type;
    private int target;

    public TreasureGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }
    
    public boolean achieved(Game game) {
        // int treasure_amount = game.getInitialTreasureCount();
        int treasure_left = game.getMap().getEntities(Treasure.class).size();

        return game.getInitialTreasureCount() - treasure_left >= target;


    }

    public int getSumOfCollectTreasure(Game game) {
        int sum = game.getMap().getEntities(Treasure.class).size();
        sum += game.getMap().getEntities(SunStone.class).size();
        return sum;
    }

    public String toString(Game game) {
        if (!this.achieved(game)) {
            return ":treasure";
        } 
        return "";
    }

    public String getType() {
        return type;
    }
}


