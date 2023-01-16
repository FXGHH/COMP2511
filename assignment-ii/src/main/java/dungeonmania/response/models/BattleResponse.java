package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;

public final class BattleResponse {
    private final String enemy;
    private final double initialPlayerHealth;
    private final double initialEnemyHealth;
    private final List<ItemResponse> battleItems;
    private final List<RoundResponse> rounds;

    public BattleResponse() {
        this.initialPlayerHealth = 0;
        this.initialEnemyHealth = 0;
        this.enemy = "";
        this.battleItems = new ArrayList<>();
        this.rounds = new ArrayList<>();
    }

    public BattleResponse(String enemy, List<RoundResponse> rounds, List<ItemResponse> battleItems,
    double initialPlayerHealth, double initialEnemyHealth) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = rounds;
        this.battleItems = battleItems;
    }

    public final String getEnemy() {
        return enemy;
    }

    public final double getInitialPlayerHealth() {
        return initialPlayerHealth;
    }

    public final double getInitialEnemyHealth() {
        return initialEnemyHealth;
    }

    public final List<RoundResponse> getRounds() {
        return rounds;
    }

    public final List<ItemResponse> getBattleItems() {
        return battleItems;
    }
}
