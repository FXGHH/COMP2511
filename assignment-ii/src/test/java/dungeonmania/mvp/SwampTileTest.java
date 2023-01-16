package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Swamp;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    @DisplayName("Test spider dwell in swamp")
    public void testSwampSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spiderSwampTest", "simpleConfig");
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        // res = dmc.tick(Direction.UP);

        // Siper move onto the swamp tile
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(new Position(5, 4), pos);

        // Get the swamp entity
        Entity e = Entity.findEntityWithPosition(new Position(6, 4));
        assertTrue(e.getType().equals("swamp_tile"));
        Swamp swamp = (Swamp) e;

        // Spider move in swamp tile
        res = dmc.tick(Direction.RIGHT);
        pos = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(new Position(6, 4), pos);


        // Check that the position of spider does not change for the duration of movement factor
        int dwellTime = swamp.getMovementFactor();
        for (int i = 0; i < dwellTime; i++) {
            res = dmc.tick(Direction.RIGHT);
            Position spider = TestUtils.getEntities(res, "spider").get(0).getPosition();
            assertTrue(spider.equals(pos));
        }

        // Check the postion of spider after leave the swamp tile
        res = dmc.tick(Direction.RIGHT);
        pos = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertNotEquals(new Position(6, 4), pos);

    }


    @Test
    @DisplayName("Test mercenary corss pass through swamp")
    public void testSwampMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampAllyTest", "simpleConfig");

        res = dmc.tick(Direction.UP);
        EntityResponse mercenary = TestUtils.getEntities(res, "mercenary").get(0);
        Position stuckPosition = mercenary.getPosition();

        // Get the swamp entity
        Entity e = Entity.findEntityWithPosition(mercenary.getPosition());
        Swamp swamp = (Swamp) e;
        assertTrue(swamp.getType().equals("swamp_tile"));

        // Check that the position of mercenary does not change for the duration of movement factor
        for (int i = 0; i < swamp.getMovementFactor(); i++) {
            res = dmc.tick(Direction.UP);
            mercenary = TestUtils.getEntities(res, "mercenary").get(0);
            assertTrue(mercenary.getPosition().equals(stuckPosition));
        }
        
        // Check that mercenary should be next to player
        res = dmc.tick(Direction.UP);
        mercenary = TestUtils.getEntities(res, "mercenary").get(0);
        assertTrue(Position.isAdjacent(mercenary.getPosition(), Entity.getPlayer().getPosition()));
    }
}
