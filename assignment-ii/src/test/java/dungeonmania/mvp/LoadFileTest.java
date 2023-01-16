package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;

public class LoadFileTest {

    @Test
    @DisplayName("Basic load file test")
    public void testSunStoneDoorUnlock() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicLoadFileTest", "simpleConfig");
        
        // Save the game and get the orginal game response
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        dmc.saveGame("SavedGame1");
        
        // Get second loaded game response and check if they're equal
        DungeonResponse res_2 = dmc.loadGame("Saved Game0");
        assertEquals(res.getDungeonName(), res_2.getDungeonName());
        assertEquals(res.getDungeonId(), res_2.getDungeonId());
        assertEquals(res.getEntities(), res_2.getEntities());
        assertEquals(res.getGoals(), res_2.getGoals());
    }
}
