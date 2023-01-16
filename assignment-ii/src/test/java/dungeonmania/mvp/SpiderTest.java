package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpiderTest {

    @Test
    @Tag("9-1")
    @DisplayName("Test basic movement of spiders")
    public void basicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y - 1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
            TestUtils.getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @Tag("9-2")
    @DisplayName("Test spiders can traverse through walls")
    public void wallMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_wallMovement", "c_spiderTest_wallMovement");

        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        // Assert Spider moves though wall
        res = dmc.tick(Direction.UP);
        assertNotEquals(pos, TestUtils.getEntities(res, "spider").get(0).getPosition());
    }

    @Test
    @Tag("9-3")
    @DisplayName("Test spiders can traverse through switches, doors and exits")
    public void switchDoorExitMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_switchDoorExitMovement", "c_spiderTest_switchDoorExitMovement");

        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        // Assert Spider moves though switch
        res = dmc.tick(Direction.UP);
        assertNotEquals(pos, TestUtils.getEntities(res, "spider").get(0).getPosition());
        pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        // Assert Spider moves though door
        res = dmc.tick(Direction.UP);
        assertNotEquals(pos, TestUtils.getEntities(res, "spider").get(0).getPosition());
        pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        // Assert Spider moves though exit
        res = dmc.tick(Direction.UP);
        assertNotEquals(pos, TestUtils.getEntities(res, "spider").get(0).getPosition());
    }

    @Test
    @Tag("9-4")
    @DisplayName("Test spiders cannot move through boulders and reverses direction")
    public void boulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_boulder", "c_spiderTest_boulder");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        int x = pos.getX();
        int y = pos.getY();
        List<Position> movementTrajectory = new ArrayList<>();
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x - 1, y - 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y - 1));
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x, y - 1));

        // Assert Circular Movement of Spider
        for (int i = 0; i < movementTrajectory.size(); ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(i), TestUtils.getEntities(res, "spider").get(0).getPosition());
        }
    }

    @Test
    @Tag("9-5")
    @DisplayName("Test spider_spawn_interval = 0 in config file")
    public void spawnRateZero() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_spiderTest_spawnRateZero", "c_spiderTest_spawnRate0");
        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(0, TestUtils.getEntities(res, "spider").size());
        }
    }

    @Test
    @Tag("9-6")
    @DisplayName("Test spider_spawn_interval = 1, 5, 10 in config file")
    public void spawnRate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_spawnRate", "c_spiderTest_spawnRate1");
        for (int i = 1; i < 20; i++) {
            res = dmc.tick(Direction.UP);

            // We must consider that spiders may battle the player and die
            assertEquals(i - getNumKilledSpiders(res), TestUtils.getEntities(res, "spider").size());
        }

        int spiderCount = 0;

        dmc = new DungeonManiaController();
        dmc.newGame("d_spiderTest_spawnRate", "c_spiderTest_spawnRate5");
        for (int i = 1; i < 30; i++) {
            res = dmc.tick(Direction.UP);
            if (i % 5 == 0) {
                spiderCount++;
            }
            // We must consider that spiders may battle the player and die
            assertEquals(spiderCount - getNumKilledSpiders(res), TestUtils.getEntities(res, "spider").size());
        }

        dmc = new DungeonManiaController();
        dmc.newGame("d_spiderTest_spawnRate", "c_spiderTest_spawnRate10");
        spiderCount = 0;
        for (int i = 1; i < 35; i++) {
            res = dmc.tick(Direction.UP);
            if (i % 10 == 0) {
                spiderCount++;
            }
            // We must consider that spiders may battle the player and die
            assertEquals(spiderCount - getNumKilledSpiders(res), TestUtils.getEntities(res, "spider").size());
        }
    }

    private int getNumKilledSpiders(DungeonResponse res) {
        // If we have had x battles and the player is still alive, we must have killed x spiders
        return res.getBattles().size();
    }
}
