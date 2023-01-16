package dungeonmania.entities.buildables;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;

public interface BuildableItem {
    public boolean canBuild();

    public String addBuildable();

    public InventoryItem build(Player p, boolean remove, boolean forceShield, EntityFactory factory);
}
