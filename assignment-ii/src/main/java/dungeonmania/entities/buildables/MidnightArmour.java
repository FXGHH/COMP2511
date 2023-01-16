package dungeonmania.entities.buildables;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;

public class MidnightArmour extends Buildable implements BattleItem {
    private int attack;
    private int defence;

    public MidnightArmour() {
        super(null, "midnight_armour");
        this.attack = getAttack();
        this.defence = getDefence();
    }

    @Override
    public void use(Game game) {

    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            this.attack,
            this.defence,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return 1;
    }

    static public List<String> checkCanBuild() {
        ArrayList<List<String>> ingredients = new ArrayList<>();
        // add all possible method into the list 
        List<String> method1 = Arrays.asList("sword", "sun_stone");
        ingredients.add(method1);
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

    public static int getAttack() {
        return EntityFactory.gteCofigs().optInt("midnight_armour_attack");
    }

    public static int getDefence() {
        return EntityFactory.gteCofigs().optInt("midnight_armour_defence");
    }
}