package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.playerState.InvincibleState;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvincibilityPotion(Position position, String nameOfEntity) {
        super(position, getPotionDuration(), nameOfEntity);
    }
    
    @Override
    public InvincibleState use(Player player) {
        return new InvincibleState(player);
    }

    public static int getPotionDuration() {
        return EntityFactory.gteCofigs().optInt(
            "invincibility_potion_duration",
            InvincibilityPotion.DEFAULT_DURATION);
    }
}
