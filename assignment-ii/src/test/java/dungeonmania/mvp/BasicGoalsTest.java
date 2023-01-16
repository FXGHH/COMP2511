package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGoalsTest {

    @Test
    @Tag("13-1")
    @DisplayName("Test achieving a basic exit goal")
    public void exit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_exit", "c_basicGoalsTest_exit");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to exit
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }


    @Test
    @Tag("13-2")
    @DisplayName("Test achieving a basic boulders goal")
    public void oneSwitch()  {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_oneSwitch", "c_basicGoalsTest_oneSwitch");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-3")
    @DisplayName("Test achieving a boulders goal where there are five switches")
    public void fiveSwitches()  {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_fiveSwitches", "c_basicGoalsTest_fiveSwitches");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move first four boulders onto switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move last boulder onto switch
        res = dmc.tick(Direction.DOWN);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }


    @Test
    @Tag("13-4")
    @DisplayName("Test achieving a basic treasure goal")
    public void treasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_treasure", "c_basicGoalsTest_treasure");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test achieving a basic enemy goal, no spawner")
    public void enemiesNoSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicEnemiesGoal_noSpawner", "c_basicEnemiesGoal");

        // move player to right
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.LEFT);
        assertEquals(2, res.getBattles().size());
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));     
        
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(3, res.getBattles().size());
        assertFalse(TestUtils.getGoals(res).contains(":enemies")); 
    }

    @Test
    @DisplayName("Test achieving a basic enemy goal, no spawner")
    public void enemiesSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicEnemiesGoal_Spawner", "c_basicEnemiesGoal");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // move player to right
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.LEFT);
        assertEquals(2, res.getBattles().size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));     
        
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(3, res.getBattles().size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies")); 

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertFalse(TestUtils.getGoals(res).contains(":enemies")); 
    }
}
