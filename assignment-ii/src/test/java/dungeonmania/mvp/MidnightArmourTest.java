package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {
    @Test
    @DisplayName("Test midnight armour can be built")
    public void midnightArmourbuild() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicMidnightArmour_build", "c_basicMidnightArmour");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test midnight armour can be built with zombies")
    public void midnightArmourNobuild() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicMidnightArmour_noBuild", "c_basicMidnightArmour");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () ->
                dmc.build("midnight_armour"));
    }
}
