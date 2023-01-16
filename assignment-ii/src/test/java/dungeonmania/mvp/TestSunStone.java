package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestSunStone {

    @Test
    @DisplayName("Test Sun stone can opne the door")
    public void testSunStoneDoorUnlock() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunstoneTest_unlock", "simpleConfig");

        // player pick up the sun stone
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // player open the door with the sun stone
        Position pos1 = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos1, TestUtils.getEntities(res, "player").get(0).getPosition());
        
        // Check the sun stone not be used 
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Player get the key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        Position pos2 = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos2, TestUtils.getEntities(res, "player").get(0).getPosition());

        // The sun stone not be used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // But use the key
        assertEquals(0, TestUtils.getInventory(res, "key").size());
    }


    @Test
    @DisplayName("Test Sun stone can be use as key and as treasure")
    public void testSunStoneCanUseAsTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunstoneTest_build_goal", "simpleConfig");

        // player pick up the sun stone
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // player get two wood x 2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        
        // Check the sun stone not be used 
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        // The sun stone not be used and wood be consumed
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Player get the key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // check if player achive the goal
        assertEquals("", TestUtils.getGoals(res));
    }
}