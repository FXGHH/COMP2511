package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PortalsTest {
    @Test
    @Tag("7-1")
    @DisplayName("Test portals work both ways")
    public void testTeleportationBothWays() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse preTeleportResponse = controller.newGame(
            "d_PortalsTest_testTeleportationBothWays", "c_PortalsTest_testTeleportationBothWays");
        EntityResponse player = TestUtils.getPlayer(preTeleportResponse).get();

        assertEquals(new Position(0, 1), player.getPosition());

        // Move right to teleport
        DungeonResponse postTeleportResponse = controller.tick(Direction.RIGHT);
        player = TestUtils.getPlayer(postTeleportResponse).get();
        Position playerPosition = player.getPosition();

        assertEquals(1, TestUtils.getManhattanDistance(new Position(4, 1), playerPosition));

        // Move to teleport back
        if (playerPosition.equals(new Position(4, 0))) {
            postTeleportResponse = controller.tick(Direction.DOWN);
        } else if (playerPosition.equals(new Position(3, 1))) {
            postTeleportResponse = controller.tick(Direction.RIGHT);
        } else if (playerPosition.equals(new Position(4, 2))) {
            postTeleportResponse = controller.tick(Direction.UP);
        } else if (playerPosition.equals(new Position(5, 1))) {
            postTeleportResponse = controller.tick(Direction.LEFT);
        }
        player = TestUtils.getPlayer(postTeleportResponse).get();
        playerPosition = player.getPosition();

        assertEquals(1, TestUtils.getManhattanDistance(new Position(1, 1), playerPosition));
    }

    @Test
    @Tag("7-2")
    @DisplayName("Test player cannot teleport when the exit portal is surrounded by walls")
    public void testCannotTeleportIntoWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_PortalsTest_testCannotTeleportIntoWall", "c_PortalsTest_testCannotTeleportIntoWall");

        DungeonResponse dungeonResponse = controller.tick(Direction.RIGHT);
        EntityResponse player = TestUtils.getPlayer(dungeonResponse).get();

        // The destination is at (4,1) but it has been surrounded by walls
        // So it cannot appears next to (4,1)
        assertNotEquals(1, TestUtils.getManhattanDistance(new Position(4, 1), player.getPosition()));
    }

    @Test
    @Tag("7-3")
    @DisplayName("Test portal has no effect on spiders")
    public void testNoEffectOnSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initState = controller.newGame(
            "d_PortalsTest_testNoEffectOnSpider", "c_PortalsTest_testNoEffectOnSpider");

        Position initSpiderPosition = TestUtils.getEntities(initState, "spider").get(0).getPosition();
        List<Position> movementTrajectory = TestUtils.getSpiderTrajectory(initSpiderPosition);

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            DungeonResponse res = controller.tick(Direction.UP);
            assertEquals(movementTrajectory.get(i % 8),
                    TestUtils.getEntities(res, "spider").get(0).getPosition());
        }
    }

    @Test
    @Tag("7-4")
    @DisplayName("Test portal has no effect on zombies")
    public void testNoEffectOnZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame(
            "d_PortalsTest_testNoEffectOnZombie", "c_PortalsTest_testNoEffectOnZombie");
        Position zombiePosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        Position portalPosition = new Position(2, 1);

        // Try at most 100 random movements
        // Early exit if the zombie moves to the portal position
        for (int i = 0; (i < 100 && !zombiePosition.equals(portalPosition)); ++i) {
            res = controller.tick(Direction.DOWN);
            zombiePosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
            assertTrue(TestUtils.getManhattanDistance(zombiePosition, portalPosition) > 0);
        }
        assertTrue(TestUtils.getManhattanDistance(zombiePosition, portalPosition) > 0);
    }

    @Test
    @Tag("7-5")
    @DisplayName("Test portal matching when there are multiple portals")
    public void testMultiplePortals() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame(
            "d_PortalsTest_testMultiplePortals", "c_PortalsTest_testMultiplePortals");

        // Move into the red portal
        res = controller.tick(Direction.RIGHT);
        Position playerPos = TestUtils.getPlayer(res).get().getPosition();
        Position destRedPortalPos = new Position(3, 3);
        assertEquals(1, TestUtils.getManhattanDistance(playerPos, destRedPortalPos));
    }

    @Test
    @Tag("7-6")
    @DisplayName("Test chain teleporting between multiple portals")
    public void testMultiplePortalsChain() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame(
            "d_PortalsTest_testMultiplePortalsChain", "c_PortalsTest_testMultiplePortalsChain");

        Position bluePortalPos = new Position(1, 1);
        Position greyPortalPos = new Position(5, 1);
        Position greenPortalPos = new Position(1, 5);
        Position yellowPortalPos = new Position(5, 5);

        // Move into the red portal
        res = controller.tick(Direction.RIGHT);
        Position playerPos = TestUtils.getPlayer(res).get().getPosition();

        // Player should end up at one of the outside portals
        assertTrue(TestUtils.getManhattanDistance(playerPos, bluePortalPos) == 1
                || TestUtils.getManhattanDistance(playerPos, greyPortalPos) == 1
                || TestUtils.getManhattanDistance(playerPos, greenPortalPos) == 1
                || TestUtils.getManhattanDistance(playerPos, yellowPortalPos) == 1);
    }

}
