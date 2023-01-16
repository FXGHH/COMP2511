package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;

public class Bow extends Buildable implements BattleItem{

    private int durability;

    public Bow() {
        super(null, "bow");
        this.durability = getBowDurability();
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
            0,
            0,
            2,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }


    static public List<String> checkCanBuild() {
        ArrayList<List<String>> ingredients = new ArrayList<>();
        List<String> method1 = Arrays.asList("wood", "arrow", "arrow", "arrow");

        ingredients.add(method1);
        for (List<String> method : ingredients) {
            if (hasEnoughitems(method)) {
                return method;
            }
        }
        return null;
        
    }

    static public List<String> getIngredients() {
        return checkCanBuild();
    }

    public static int getBowDurability() {
        return EntityFactory.gteCofigs().optInt("bow_durability");
    }

}
