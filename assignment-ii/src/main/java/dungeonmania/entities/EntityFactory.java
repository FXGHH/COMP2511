package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.*;
import dungeonmania.map.GameMap;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class EntityFactory {
    private static JSONObject config;
    private Random ranGen = new Random();

    public EntityFactory(JSONObject config) {
        EntityFactory.config = config;
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0) return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();
        Spider dummySpider = new Spider(new Position(0, 0), "spider");

        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius) continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np)) continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = new Spider(initPosition, "spider");
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0) return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> !map.getEntities(p).stream().anyMatch(e -> (e instanceof Wall)))
            .collect(Collectors.toList());
        if (pos.size() == 0) return;
        ZombieToast zt = new ZombieToast(pos.get(randGen.nextInt(pos.size())), "zombie_toast");
        map.addEntity(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));
        String nameOfEntity = jsonEntity.getString("type");
        switch (jsonEntity.getString("type")) {
            case "player":
                return new Player(pos, nameOfEntity);
            case "zombie_toast":
                return new ZombieToast(pos, nameOfEntity);
            case "zombie_toast_spawner":
                return new ZombieToastSpawner(pos, nameOfEntity);
            case "mercenary":
                return new Mercenary(pos, nameOfEntity);
            case "wall":
                return new Wall(pos, nameOfEntity);
            case "boulder":
                return new Boulder(pos, nameOfEntity);
            case "switch":
                return new Switch(pos, nameOfEntity);
            case "exit":
                return new Exit(pos, nameOfEntity);
            case "treasure":
                return new Treasure(pos, nameOfEntity);
            case "wood":
                return new Wood(pos, nameOfEntity);
            case "arrow":
                return new Arrow(pos, nameOfEntity);
            case "bomb":
                int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
                return new Bomb(pos, bombRadius, nameOfEntity);

            case "invisibility_potion":
                return new InvisibilityPotion(pos, nameOfEntity);
            case "invincibility_potion":
                return new InvincibilityPotion(pos, nameOfEntity);
            case "portal":
                return new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour")), nameOfEntity);
            case "sword":
                return new Sword(pos, nameOfEntity);
            case "spider":
                return new Spider(pos, nameOfEntity);
            case "assassin":
                return new Assassin(pos, nameOfEntity);
            case "door":
                return new Door(pos, jsonEntity.getInt("key"), nameOfEntity);
            case "key":
                return new Key(pos, jsonEntity.getInt("key"), nameOfEntity);
            case "sun_stone":
                return new SunStone(pos, nameOfEntity);
            case "swamp_tile":
                return new Swamp(pos, nameOfEntity);
            default:
                return null;
        }
    }

    static public JSONObject gteCofigs(){
        return config;
    }

}
