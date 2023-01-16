package dungeonmania.goals;

import dungeonmania.Game;

public interface Goal {

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game);
    public String toString(Game game);

}
