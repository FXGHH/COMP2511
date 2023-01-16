package dungeonmania.playerState;

import dungeonmania.battles.BattleStatistics;


public interface PlayerState {
    public String getStateName();
    public BattleStatistics stateBuff(BattleStatistics origin);
}
