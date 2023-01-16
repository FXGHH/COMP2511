package dungeonmania.entities.buildables;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;

public class Shield extends Buildable implements BattleItem {
    private int durability;
    private double defence;

    public Shield() {
        super(null, "shield");
        this.durability = getDurability();
        this.defence = getShieldDefence();
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
            defence,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    static public List<String> checkCanBuild() {
        ArrayList<List<String>> ingredients = new ArrayList<>();
        // add all possible method into the list 
        List<String> method1 = Arrays.asList("wood", "wood", "treasure");
        ingredients.add(method1);
        List<String> method2 = Arrays.asList("wood", "wood", "key");
        ingredients.add(method2);
        // check if we have enough ingredient
        for (List<String> method : ingredients) {
            if (hasEnoughitems(method)) {
                return method;
            }
        }
        return sunStoneReplacement(ingredients);
    }

    static public List<String> getIngredients() {
        return checkCanBuild();
    }

    public static int getShildDurability() {
        return EntityFactory.gteCofigs().optInt("shield_durability");
    }

    public static double getShieldDefence() {
        return EntityFactory.gteCofigs().optInt("shield_defence");
    }
}
