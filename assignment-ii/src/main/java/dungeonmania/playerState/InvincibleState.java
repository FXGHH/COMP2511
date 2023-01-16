package dungeonmania.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvincibleState implements PlayerState{
    private Player player;

    @Override
    public String getStateName() {
        return "Invincible";
    }

    public InvincibleState(Player player) {
        this.player = player;
    }

    @Override
    public BattleStatistics stateBuff(BattleStatistics origin) {
        
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                    0,
                    0,
                    0,
                    1,
                    1,
                    true,
                    true));
    }
    
    public Player getPlayer() {
        return player;
    }

}
