package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.playerState.InvisibleState;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvisibilityPotion(Position position, String nameOfEntity) {
        super(position, getPotionDuration(), nameOfEntity);
    }

    @Override
    public InvisibleState use(Player player) {
        return new InvisibleState(player);
    }

    public static int getPotionDuration() {
        return EntityFactory.gteCofigs().optInt(
            "invisibility_potion_duration",
            InvisibilityPotion.DEFAULT_DURATION);
    }
}
