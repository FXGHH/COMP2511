package dungeonmania.entities;

import dungeonmania.map.GameMap;
import java.util.List;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.Position;

public class Door extends Entity {
    private boolean open = false;
    private int number;

    public Door(Position position, int number, String nameOfEntity) {
        super(position.asLayer(Entity.DOOR_LAYER), nameOfEntity);
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && canOpen((Player) entity));
    }

    @Override
    // return false because player can't destory this door entity
    public boolean onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return false;

        Player player = (Player) entity;
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);

        if (canOpen(player)) {
            if (hasSunStone(inventory)) {
                open();
            }
            inventory.remove(key);
            open();
        }
        return false;
    }

    private boolean canOpen(Player player) {
        Inventory inventory = player.getInventory();
        if (hasSunStone(inventory)) {
            return true;
        }
        if (hasKey(inventory)) {
            return true;
        }
        return false;
    }

    public boolean hasKey(Inventory inventory) {
        List<Key> keyList = inventory.getEntities(Key.class);
        return keyList.stream().anyMatch(k -> k.getnumber() == number);
    }

    public boolean hasSunStone(Inventory inventory){
        return inventory.count(SunStone.class) > 0 ? true : false;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    // @Override
    // public HashMap<String, String> getEntityAttribute() {
    //     HashMap<String, String> attribute = new HashMap<String, String>();
    //     attribute.put("open", String.valueOf(this.open));
    //     attribute.put("number", String.valueOf(this.number));
    //     return attribute;
    // }

    // @Override
    // public void setAttribute(HashMap<String, String> attributes) {
    //     this.open = Boolean.parseBoolean(attributes.get("open"));
    //     this.number = Integer.parseInt(attributes.get("number"));
    // }
}
