package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Assassin;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public class Game implements GameSubject{
    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    static private Player player;
    private BattleFacade battleFacade;
    private int initialTreasureCount;
    private EntityFactory entityFactory;
    private boolean isInTick = false;
    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;

    private int tickCount = 0;
    private int battle_count = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();
    private ArrayList<GameObserver> listObservers = new ArrayList<GameObserver>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        player = map.getPlayer();
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");

        for (Mercenary merc : map.getEntities(Mercenary.class)) {
            attach(merc);
        }


        for (Assassin assassin : map.getEntities(Assassin.class)) {
            attach(assassin);
        }
        setTreasureNumber();
    }

    public Game tick(Direction movementDirection) {
        registerOnce(
        () -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    /**
     * Battle between player and enemy
     * @param player
     * @param enemy
     */
    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(enemy);
            battle_count++;
        }
    }

    // ADDED  BY CALVIN
    public int battleCount() {
        return battle_count;
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables();
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }

        if (buildable.equals("midnight_armour") && this.getMap().getEntities(ZombieToast.class).size() != 0) {
            throw new InvalidActionException(String.format("%s cannot be built due to zombies", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(
            () -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerBuildsItem");
        
        if (e.getClass().equals(ZombieToastSpawner.class)) {
            map.destroyEntity(e);
        }
        tick();
        return this;
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return map.countEntities(type);
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public Inventory getInventory () {
        return player.getInventory();
    }

    public List<Entity> getEntities(){
        return player.getInventory().getEntities();
    }

    public List<Entity> getMapEntity() {
        return map.getEntities();
    }

    public int tick() {
        isInTick = true;
        sub.forEach(s -> s.run());
        isInTick = false;
        sub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub.removeIf(s -> !s.isValid());
        tickCount++;
        // update the weapons/potions duration
        notifyObservers();
        return tickCount;
    }

    public int getTick() {
        return this.tickCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Game.player = player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public void setTreasureNumber() {
        this.initialTreasureCount = map.getEntities(Treasure.class).size();
        this.initialTreasureCount += map.getEntities(SunStone.class).size();
    }

    public int getInitialTreasureCount() {
        return initialTreasureCount;
    }

    public DungeonResponse createDungeonResponse() {

        List<EntityResponse> entities = getEntityResponses();
        List<ItemResponse> items = getItemResponses();
        List<BattleResponse> battles = getBattleResponses();
        String goal = goals.toString(this);
        List<String> buildables = new ArrayList<String>(getPlayer().getBuildables());

        return new DungeonResponse(id, name, entities, items, battles, buildables, goal);
    }

    public List<ItemResponse> getItemResponses() {
        return getPlayer().getItemResponses();
    }

    public List<BattleResponse> getBattleResponses() {
        return getBattleFacade().getBattleResponses();
    }

    public List<EntityResponse> getEntityResponses() {

        ArrayList<EntityResponse> responseList = new ArrayList<EntityResponse>();
        List<Entity> allEntities = getMapEntity();
        EntityResponse res;

        // for (Entity e : allEntities) {
        //     if (e instanceof Player) {
        //         res = new EntityResponse(e.getId(), e.getType(), e.getPosition().asLayer(5), false, e.getEntityAttribute());
        //     } else if (e instanceof Portal) {
        //         Portal p = (Portal) e;
        //         res = new EntityResponse(p.getId(), p.getType() + "+" + p.getColor(), p.getPosition(), false);
        //     } else if (e instanceof Boulder) {
        //         res = new EntityResponse(e.getId(), e.getType(), e.getPosition().asLayer(1), false);
        //     } else if (e instanceof Mercenary) {
        //         Mercenary m = (Mercenary) e;
        //         res = new EntityResponse(m.getId(), m.getType() + "+" + m.isAllied(), m.getPosition(), true);
        //     } else if (e instanceof Door && ((Door) e).isOpen()) {
        //         res = new EntityResponse(e.getId(), e.getType() + "+open", e.getPosition(), false);
        //     } else {
        //         res = new EntityResponse(e.getId(), e.getType(), e.getPosition(), false);
        //     }
        for (Entity e : allEntities) {
            if (e instanceof Player) {
                res = new EntityResponse(e.getId(), e.getType(), e.getPosition().asLayer(5), false);
            } else if (e instanceof Boulder) {
                res = new EntityResponse(e.getId(), e.getType(), e.getPosition().asLayer(1), false);
            } else {
                res = new EntityResponse(e.getId(), e.getType(), e.getPosition(), false);
            }
            responseList.add(res);
        }
        return responseList;
    }

	@Override
	public void attach(GameObserver o) {
		if(! listObservers.contains(o)) { listObservers.add(o); }
	}

	@Override
	public void detach(GameObserver o) {
		listObservers.remove(o);
	}

    @Override
    public void notifyObservers() {
		for( GameObserver obs : listObservers) {
			obs.update(this);
		}
    }


}
