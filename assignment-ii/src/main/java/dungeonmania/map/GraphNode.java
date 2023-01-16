package dungeonmania.map;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class GraphNode {
    private Position position;
    private List<Entity> entities = new ArrayList<>();

    private int weight = 1;

    public GraphNode(Entity entity, int weight) {
        this(entity, entity.getPosition(), weight);
    }

    public GraphNode(Entity entity) {
        this(entity, entity.getPosition(), 1);
    }

    public GraphNode(Entity entity, Position p, int weight) {
        this.position = p;
        this.entities.add(entity);
        this.weight = weight;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entities.size() == 0 || entities.stream().allMatch(e -> e.canMoveOnto(map, entity));
    }

    public int getWeight() {
        return weight;
    }

    public void addEntity(Entity entity) {
        if (!this.entities.contains(entity))
            this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public int size() {
        return entities.size();
    }

    public void mergeNode(GraphNode node) {
        List<Entity> es = node.entities;
        es.forEach(this::addEntity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Position getPosition() {
        return position;
    }
}
