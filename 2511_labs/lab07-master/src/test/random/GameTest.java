package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    @Test
    public void Test1() {
        Game game = new Game(4);
        assertFalse(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
    }

    @Test
    public void Test2() {
        Game game = new Game(-4);
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
    }
}