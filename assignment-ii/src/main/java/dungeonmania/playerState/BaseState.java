package dungeonmania.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class BaseState implements PlayerState {
    private Player player;
    
    public BaseState(Player player) {
        this.player = player;
    }

    public String getStateName() {
        return "Base";
    }

    @Override
    public BattleStatistics stateBuff(BattleStatistics origin) {
        return origin;
    }

    public Player getPlayer() {
        return player;
    }

    
}
