package dungeonmania.entities.collectables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Sword extends Entity implements InventoryItem, BattleItem {

    private int durability;
    private double attack;

    public Sword(Position position, String nameOfEntity) {
        super(position, nameOfEntity);
        this.attack = getAttack();
        this.durability = getSwordDuration();
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
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            attack,
            0,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public static double getAttack() {
        return EntityFactory.gteCofigs().optDouble("sword_attack");
    }
    //Sword.DEFAULT_ATTACK
    public static int getSwordDuration() {
        return EntityFactory.gteCofigs().optInt("sword_durability");
    }
}
