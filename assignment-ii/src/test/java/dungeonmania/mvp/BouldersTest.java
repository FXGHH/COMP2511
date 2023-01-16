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

public class BouldersTest {
    private boolean boulderAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "boulder").anyMatch(
            it -> it.getPosition().equals(pos)
        );
    }

    @Test
    @Tag("3-1")
    @DisplayName("Test pushing a boulder")
    public void pushBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_pushBoulder", "c_boulderTest_pushBoulder");
        assertTrue(boulderAt(res, 1, 0));

        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 0));
        assertEquals(new Position(1, 0), TestUtils.getPlayer(res).get().getPosition());
    }

    @Test
    @Tag("3-2")
    @DisplayName("Test attempting to push a boulder into a wall")
    public void pushBoulderWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_pushBoulderWall", "c_boulderTest_pushBoulderWall");

        Position playerStart = TestUtils.getEntities(res, "player").get(0).getPosition();
        Position boulderStart = TestUtils.getEntities(res, "boulder").get(0).getPosition();

        // Player fails to move boulder
        res = dmc.tick(Direction.RIGHT);
        assertEquals(playerStart, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(boulderStart, TestUtils.getEntities(res, "boulder").get(0).getPosition());
    }

    @Test
    @Tag("3-3")
    @DisplayName("Test the player is unable to push two boulders")
    public void twoBoulders() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_twoBoulders", "c_boulderTest_twoBoulders");

        Position playerStart = TestUtils.getEntities(res, "player").get(0).getPosition();
        List<Position> bouldersStart = new ArrayList<>();
        bouldersStart.add(TestUtils.getEntities(res, "boulder").get(0).getPosition());
        bouldersStart.add(TestUtils.getEntities(res, "boulder").get(1).getPosition());

        // Player fails to move boulders
        res = dmc.tick(Direction.RIGHT);
        assertEquals(playerStart, TestUtils.getEntities(res, "player").get(0).getPosition());
        List<Position> bouldersAfter = new ArrayList<>();
        bouldersAfter.add(TestUtils.getEntities(res, "boulder").get(0).getPosition());
        bouldersAfter.add(TestUtils.getEntities(res, "boulder").get(1).getPosition());
        assertEquals(bouldersStart, bouldersAfter);
    }
}
