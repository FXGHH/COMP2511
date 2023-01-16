package dungeonmania.goals;


import dungeonmania.Game;

public class And implements Goal {
    private String type;
    private Goal goal1;
    private Goal goal2;

    public And(String type, Goal goal1, Goal goal2) {
        this.type = type;
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    public String toString(Game game) {
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

    public String getType() {
        return type;
    }
}