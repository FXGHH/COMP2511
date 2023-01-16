package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.util.Position;

public class Assassin extends Mercenary{
    
    private double bribeFailRate = getbribeFailRate ();
    private boolean allied  = false;

    public Assassin(Position position, String nameOfEntity) {
        // super(position, getHealth(), getAttack(), getBribeAmount(), getBribeRadius());
        super(position, nameOfEntity);
        this.bribeFailRate = getbribeFailRate();
    }

    @Override
    public void interact(Player player, Game game) {
        int sceptre_size = player.getInventory().getEntities(Sceptre.class).size();
        if (sceptre_size != 0) {
            Sceptre sceptre= player.getInventory().getEntities(Sceptre.class).get(0);
            setMindControlDuration(sceptre.getMindControlDuration());
            setAllied(true);
        } else {
            bribe(player);
            setAllied(hasBeenBribed());
            setBribed(getAllied());
        }
    }
    
    public boolean isAllied() {
        return allied;
    }

    boolean hasBeenBribed() {
        Random rnd = new Random();
        return (this.bribeFailRate * 100 < rnd.nextInt(100));
    }

    public static double getAttack() {
        return EntityFactory.gteCofigs().optDouble("assassin_attack");
    }

    public static double getHealth() {
        return EntityFactory.gteCofigs().optDouble("assassin_health");
    }

    public static int getBribeAmount() {
        return EntityFactory.gteCofigs().optInt("assassin_bribe_amount");
    }

    public static int getBribeRadius() {
        return EntityFactory.gteCofigs().optInt("bribe_radius");
    }

    public static double getbribeFailRate () {
        return EntityFactory.gteCofigs().optDouble("assassin_bribe_fail_rate");
    }
}
