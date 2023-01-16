package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceptreTest {
    @Test
    @DisplayName("Test mercenary is mindcontrolled")
    public void sceptreMerc() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicSceptre_merc", "c_basicSceptre");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(3, 1), getMercPos(res));

        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
    }

    @Test
    @DisplayName("Test assassin is mindcontrolled")
    public void sceptreAssassin() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicSceptre_assassin", "c_basicSceptre");
        String mercId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());

        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
    }
    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
