package dungeonmania.entities.collectables.potions;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.playerState.PlayerState;
import dungeonmania.util.Position;

public abstract class Potion extends Entity implements InventoryItem, BattleItem {
    private int duration;

    public Potion(Position position, int duration, String type) {
        super(position, type);
        this.duration = duration;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public boolean onOverlap(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void use(Game game) {
        return;
    }

    public abstract PlayerState use(Player player);

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public int getDurability() {
        return 1;
    }
}
