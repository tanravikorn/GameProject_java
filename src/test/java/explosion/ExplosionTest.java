package explosion;
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;


public class ExplosionTest {
    private Board board;
    private final int ROWS = 5;
    private final int COLS = 5;

    @BeforeEach
    void setup(){
        board = new Board(ROWS, COLS);
        for(int r =0;r < ROWS;r++){
            for(int c =0;c< COLS;c++){
                board.setCandy(r,c,new Candy(r,c,CandyColor.RED));
            }
        }
    }

    @Test
    void normalBombTest(){
        Candy c1 = board.getCandy(1,0);
        Candy c2 = board.getCandy(3,2);
        Candy c3 = board.getCandy(1,4);
        Set<Point> removes = new HashSet<>();
        c1.performExplosion(board,removes);
        c2.performExplosion(board,removes);
        c3.performExplosion(board,removes);

        assertTrue(removes.contains(new Point(1,0)));
        assertTrue(removes.contains(new Point(3,2)));
        assertTrue(removes.contains(new Point(1,4)));
    }

    @Test
    void bombTest(){
        Candy candy = board.getCandy(2,2);
        candy.setType(CandyType.BOMB);
        Set<Point> removes = new HashSet<>();
        candy.performExplosion(board,removes);
        assertEquals(9, removes.size());
        assertTrue(removes.contains(new Point(1,1)));
        assertTrue(removes.contains(new Point(2,2)));
        assertTrue(removes.contains(new Point(3,3)));
        assertFalse(removes.contains(new Point(0,2)));
        assertFalse(removes.contains(new Point(4,4)));
        assertFalse(removes.contains(new Point(4,3)));
        assertFalse(removes.contains(new Point(2,4)));
    }

    @Test
    void lineExplosionTest(){
        Candy candy = board.getCandy(0,0);
        candy.setType(CandyType.STRIPED_HOR);
        Set<Point> removes = new HashSet<>();
        candy.performExplosion(board, removes);
        for(int c =0;c < COLS;c++){
            assertTrue(removes.contains(new Point(0,c)));
        }

        removes.clear();
        candy.setType(CandyType.STRIPED_VER);
        candy.performExplosion(board, removes);
        for (int r =0;r < ROWS;r++){
            assertTrue(removes.contains(new Point(r,0)));
        }
    }

    @Test
    void colorBombTest(){
        Candy candy = board.getCandy(0,0);
        candy.setType(CandyType.COLOR_BOMB);
        candy.prepareColorBomb(CandyColor.RED);
        Set<Point> removes = new HashSet<>();
        candy.performExplosion(board, removes);
        assertEquals(25,removes.size());
    }

    @Test
    void frozenBombTest(){
        Candy c1 = board.getCandy(2,2);
        Candy c2 = board.getCandy(2,1);
        Candy c3 = board.getCandy(1,2);
        c1.setType(CandyType.BOMB);
        Set<Point> removes = new HashSet<>();
        c2.setFrozen(true);
        c3.setFrozen(true);
        assertTrue(c2.isFrozen());
        removes.clear();
        c1.performExplosion(board,removes);

        assertFalse(c2.isFrozen());
        assertFalse(c3.isFrozen());
        assertFalse(removes.contains(new Point(2,1)));
    }

}
