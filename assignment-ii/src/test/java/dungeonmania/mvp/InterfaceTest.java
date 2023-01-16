package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InterfaceTest {
    @Test
    @Tag("2-1")
    @DisplayName("Testing newGame interface method, normal operation")
    public void testNewGameInterfaceNormalOperation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
             "d_InterfaceTest_testNewGameInterfaceNormalOperation",
             "c_InterfaceTest_testNewGameInterfaceNormalOperation");

        assertEquals("d_InterfaceTest_testNewGameInterfaceNormalOperation", res.getDungeonName());
        assertTrue(res.getInventory().isEmpty());
        assertEquals(2, TestUtils.countType(res, "wall"));
        assertEquals(1, TestUtils.countType(res, "player"));
        assertEquals(1, TestUtils.countType(res, "exit"));
        assertNotEquals("", res.getDungeonId());
        assertNotEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2-2")
    @DisplayName("Testing newGame on a nonexistent dungeon")
    public void testNewGameInterfaceDungeonNotFound() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () ->
            dmc.newGame(
                "d_non_exist", "c_non_exist")
        );
    }

    @Nested
    class TickInterfaceTest {
        @Test
        @Tag("2-3")
        @DisplayName("Testing tick interface method, normal operation")
        public void testTickInterfaceNormalOperation() {
            DungeonManiaController dmc = new DungeonManiaController();
            DungeonResponse res = dmc.newGame(
                "d_InterfaceTest_testTickInterfaceNormalOperation", "c_InterfaceTest_testTickInterfaceNormalOperation");
            assertNotNull(res);
            assertDoesNotThrow(() -> {
                assertNotNull(dmc.tick(Direction.UP));
            });
        }

        @Test
        @Tag("2-4")
        @DisplayName("Testing tick with an unusable item in the inventory")
        public void testTickInterfaceInvalidItem() {
            DungeonManiaController dmc = new DungeonManiaController();
            dmc.newGame("d_InterfaceTest_testTickInterfaceInvalidItem", "c_InterfaceTest_testTickInterfaceInvalidItem");
            // Pick up the arrow
            DungeonResponse pickedUpArrowState = dmc.tick(Direction.RIGHT);
            assertThrows(IllegalArgumentException.class, () ->
                // try to use arrow but can't
                dmc.tick(TestUtils.getFirstItemId(pickedUpArrowState, "arrow"))
            );
        }

        @Test
        @Tag("2-5")
        @DisplayName("Testing tick with an item that is not in the player's inventory")
        public void testTickInterfaceItemNotInInventory() {
            DungeonManiaController dmc = new DungeonManiaController();
            DungeonResponse res = dmc.newGame(
                "d_InterfaceTest_testTickInterfaceItemNotInInventory",
                "c_InterfaceTest_testTickInterfaceItemNotInInventory");
            assertThrows(InvalidActionException.class, () -> {
                // try to use bomb but it is not in the inventory
                String id = TestUtils.getEntities(res, "bomb").get(0).getId();
                dmc.tick(id);
            });
        }

    }
}
