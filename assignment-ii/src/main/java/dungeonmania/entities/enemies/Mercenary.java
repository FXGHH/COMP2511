package dungeonmania.entities.enemies;

import java.util.List;
import dungeonmania.Game;
import dungeonmania.GameObserver;
import dungeonmania.GameSubject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, GameObserver {
    private int bribeAmount = getBribeAmount();
    private int bribeRadius = getBribeRadius();
    private boolean allied = false;
    // private MoveAction moveAction;
    private boolean teleportToAlly;
    private boolean bribed;
    private int mindControlDuration;

    public Mercenary(Position position, String nameOfEntity) {
        super(position, getHealth(), getAttack(), nameOfEntity);
        this.bribeAmount = getBribeAmount();
        this.bribeRadius = getBribeRadius();
        this.allied = false;
        this.teleportToAlly = false;
        this.bribed = false;
        this.mindControlDuration = 0;
    }

    public boolean isAllied() {
        return this.allied;
    }

    @Override
    public boolean onOverlap(GameMap map, Entity entity) {
        if (this.allied) return false;
        super.onOverlap(map, entity);
        return false;
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    public boolean canBeBribed(Player player) {
        return (Position.calculateDistanceFrom(player.getPosition(), this.getPosition())) <= this.bribeRadius && player.countEntityOfType(Treasure.class) >= this.bribeAmount;
    }

    /**
     * bribe the merc
     */
    public void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        int sceptre_size = player.getInventory().getEntities(Sceptre.class).size();
        if (sceptre_size != 0) {
            Sceptre sceptre= player.getInventory().getEntities(Sceptre.class).get(0);
            mindControlDuration = sceptre.getMindControlDuration();
            this.allied = true;
        } else {
            this.allied = true;
            this.bribed = true;
            bribe(player);
        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();

        List<Position> pos = getPosition().getCardinallyAdjacentPositions();
        Boolean nextToPlayer = pos
            .stream()
            .anyMatch(p -> p.equals(map.getPlayer().getPosition()));
        
        if (nextToPlayer) {
            this.teleportToAlly = true;
        }

        if (this.allied) {
            if (this.teleportToAlly) {
                nextPos = map.getPlayer().getPreviousDistinctPosition();
            } else {
                nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
            }

            map.moveTo(this, nextPos);

            pos = getPosition().getCardinallyAdjacentPositions();
            nextToPlayer = pos
                .stream()
                .anyMatch(p -> p.equals(map.getPlayer().getPosition()));
            
            if (nextToPlayer) {
                this.teleportToAlly = true;
            }

        } else {
            // Follow hostile
            nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
            map.moveTo(this, nextPos);
        }
        // map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        int sceptre_size = player.getInventory().getEntities(Sceptre.class).size();
        return !this.allied && (canBeBribed(player) || sceptre_size != 0);
    }

    public boolean isAdjacent(Player player) {
        Position mercenaryPostion = this.getPosition();
        Position playerPostion = player.getPosition();
        int xDiff = mercenaryPostion.getX() - playerPostion.getX();
        int yDiff = mercenaryPostion.getY() - playerPostion.getY();
        return (Math.abs(xDiff) + Math.abs(yDiff) <= 1) ? true : false;
    }

    public static double getHealth() {
        return EntityFactory.gteCofigs().optDouble("mercenary_health");
    }

    public static double getAttack() {
        return EntityFactory.gteCofigs().optDouble("mercenary_attack");
    }

    public static int getBribeAmount() {
        return EntityFactory.gteCofigs().optInt("bribe_amount");
    }

    public static int getBribeRadius() {
        return EntityFactory.gteCofigs().optInt("bribe_radius");
    }

    // @Override
    // public HashMap<String, String> getEntityAttribute() {
    //     HashMap<String, String> attribute = new HashMap<String, String>();
    //     attribute.put("allied", String.valueOf(this.allied));
    //     attribute.put("teleportToAlly", String.valueOf(this.teleportToAlly));
    //     return attribute;
    // }

    // @Override
    // public void setAttribute(HashMap<String, String> attributes) {
    //     this.allied = Boolean.parseBoolean(attributes.get("allied"));
    //     this.teleportToAlly = Boolean.parseBoolean(attributes.get("teleportToAlly"));
    // }

    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    public boolean getAllied(){
        return this.allied;
    }

    public void setTeleportToAlly(boolean setTeleportToAlly) {
        this.teleportToAlly = setTeleportToAlly;
    }

    public boolean setTeleportToAlly() {
        return this.teleportToAlly;
    }

    public void setMindControlDuration(int mindControlDuration) {
        this.mindControlDuration = mindControlDuration;
    }

    public void setBribed(boolean bribed) {
        this.bribed = bribed;
    }

    @Override
    public void update(GameSubject obj) {
        if (!bribed && mindControlDuration != 0) {
            this.mindControlDuration--;
        } else if (!bribed && this.mindControlDuration == 0) {
            setAllied(false);
        }
    }
}
