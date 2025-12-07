import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.Point;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class CandyTest {
    Candy candy;
    @BeforeEach
    void setup(){
        candy = new Candy(2,3, CandyColor.BLUE);
    }
    @Test
    void candyConstructureTest(){
        assertEquals(2,candy.getRow());
        assertEquals(3,candy.getColumn());
        assertEquals(CandyType.NORMAL, candy.getType());
        assertFalse(candy.isFrozen());
        assertEquals(CandyColor.BLUE, candy.getColor());
    }

    @Test
    void setterTest(){
        candy.setRow(5);
        candy.setColumn(7);
        assertEquals(5, candy.getRow());
        assertEquals(7, candy.getColumn());

        candy.setFrozen(true);
        assertTrue(candy.isFrozen());
        candy.setType(CandyType.BOMB);
        assertEquals(CandyType.BOMB, candy.getType());

        candy.setColor(CandyColor.GREEN);
        assertEquals(CandyColor.GREEN, candy.getColor());

    }
    @Test
    void testPrepareColorBombSafeCheck() {
        candy.setType(CandyType.NORMAL);
        assertDoesNotThrow(() -> {
            candy.prepareColorBomb(CandyColor.BLUE);
        });

        candy.setType(CandyType.COLOR_BOMB);
        assertDoesNotThrow(() -> {
            candy.prepareColorBomb(CandyColor.BLUE);
        });
    }

    @Test
    void testPerformExplosion(){
        Board board = new Board(5, 5);
        Set<Point> affects = new HashSet<>();

        assertDoesNotThrow(() -> {
            candy.performExplosion(board, affects);
        });

        candy.setType(CandyType.BOMB);
        assertDoesNotThrow(() -> {
            candy.performExplosion(board, affects);
        });
    }

}
