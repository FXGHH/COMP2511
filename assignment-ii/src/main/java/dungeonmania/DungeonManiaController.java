package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import dungeonmania.entities.buildables.Buildable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    static private Game currGame;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            currGame = builder.buildGame(configName, dungeonName);
            return ResponseBuilder.getDungeonResponse(currGame);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return null;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(currGame.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(currGame.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuildables = Buildable.getBuildables();
        if (!validBuildables.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }

        return ResponseBuilder.getDungeonResponse(currGame.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(currGame.interact(entityId));
    }


    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        File file = new File("src/main/java/dungeonmania/save/" + name + ".json");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream("src/main/java/dungeonmania/save/" + name + ".json");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
                
            oos.writeObject(currGame);
            
            oos.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currGame.createDungeonResponse();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {

        try {
            FileInputStream fis = new FileInputStream("src/main/java/dungeonmania/save/" + name + ".json");
            ObjectInputStream ois = new ObjectInputStream(fis);
            currGame = (Game) ois.readObject();

            fis.close();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currGame.createDungeonResponse();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return new ArrayList<>();
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(
            int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        return null;
    }

    static public Game getCurrGame() {
        return currGame;
    }

}
