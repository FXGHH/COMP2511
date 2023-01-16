package dungeonmania.entities;

import dungeonmania.Game;


public interface Interactable {
    public void interact(Player player, Game game);
    public boolean isInteractable(Player player);
}
