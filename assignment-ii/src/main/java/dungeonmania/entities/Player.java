package dungeonmania.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.playerState.BaseState;
import dungeonmania.playerState.PlayerState;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable {
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    private PlayerState state;

    public Player(Position position, String nameOfEntity) {
        super(position, nameOfEntity);
        battleStatistics = new BattleStatistics(
                getHealth(),
                getAttack(),
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        // battleStatistics = new BattleStatistics(health, attack);
        inventory = new Inventory();
        state = new BaseState(this);
    }

    public PlayerState getPlayerState(){
        return state;
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public BattleItem getWeapon() {
        return inventory.getWeapon();
    }

    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

    public boolean build(String entity, EntityFactory factory) {
        InventoryItem item = inventory.checkBuildCriteria(this, entity, factory);
        if (item == null) return false;
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public boolean onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied()) return false;
            }
            map.getGame().battle(this, (Enemy) entity);
        }
        return false;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public void pickUp(Entity item, GameMap map) {

        if (item.onOverlap(map, this)) {
            inventory.add((InventoryItem) item);
            map.destroyEntity(item);
        }
        return;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null) inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;

            // state.transitionBase();
            state = new BaseState(this);

            return;
        }
        inEffective = queue.remove();
        state = inEffective.use(this);
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public List<ItemResponse> getItemResponses() {
        ArrayList<ItemResponse> responseList = new ArrayList<ItemResponse>();

        for (Entity e : getInventory().getEntities()) {
            ItemResponse iResponse = new ItemResponse(e.getId(), e.getType());
            responseList.add(iResponse);
        }
        return responseList;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return state.stateBuff(origin);
    }

    public static double getAttack() {
        return EntityFactory.gteCofigs().optDouble("player_attack");
    }

    public static double getHealth() {
        return EntityFactory.gteCofigs().optDouble("player_health");
    }
}
