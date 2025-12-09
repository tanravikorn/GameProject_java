package test.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.explosion.*;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


import java.util.HashSet;
import java.util.Set;

public class ComboTest {
    private Board board;
    private Set<Point> removes;
    @BeforeEach
    void setup(){
        board = new Board(5,5);
        for(int r =0;r < 5;r++){
            for(int c =0;c < 5;c++){
                Candy candy = new Candy(r,c,CandyColor.BLUE);
                board.setCandy(r,c,candy);
            }
        }
        removes = new HashSet<>();
    }

    @Test
    void stripedComboTest(){
        ExplosionStrategy cross = new CrossExplosion();
        cross.explode(board,2,2, removes);
        for(int r = 0;r < 5;r++){
            assertTrue(removes.contains(new Point(r,2)));
        }
        for(int c =0;c < 5;c++){
            assertTrue(removes.contains(new Point(2,c)));
        }
    }

    @Test
    void bombBombComboTest(){
        ExplosionStrategy bigBomb = new AreaExplosion(2);
        bigBomb.explode(board,2,2,removes);
        assertEquals(25, removes.size());
        assertTrue(removes.contains(new Point(0,0)));
        assertTrue(removes.contains(new Point(1,3)));
    }
    @Test
    void nukeComboTest(){
        ExplosionStrategy nuke = new NukeExplosion();
        nuke.explode(board,1,1,removes);
        assertEquals(25,removes.size());
        assertTrue(removes.contains(new Point(0,0)));
        assertTrue(removes.contains(new Point(1,3)));
    }
    @Test
    void stripedColorComboTest(){
        Candy c1 = board.getCandy(0,0);
        c1.setColor(CandyColor.GREEN);
        ExplosionStrategy colorStriped = new ColorStripedExplosion();
        ((ColorStripedExplosion) colorStriped).setTargetColor(CandyColor.GREEN);
        colorStriped.explode(board, 2,2,removes);
        assertTrue(removes.contains(new Point(2,2)));
        assertTrue(removes.contains(new Point(0,0)));
        assertTrue(removes.contains(new Point(1,0)) || removes.contains(new Point(0,1)));
        assertTrue(removes.contains(new Point(3,0)) || removes.contains(new Point(0,3)));
        assertTrue(removes.contains(new Point(4,0)) || removes.contains(new Point(0,4)));
    }
    @Test
    void colorBombBombTest(){
        Candy c1 = board.getCandy(2,2);
        c1.setColor(CandyColor.GREEN);
        ExplosionStrategy colorBombBomb = new ColorBombBombExplosion();
        ((ColorBombBombExplosion) colorBombBomb).setTargetColor(CandyColor.GREEN);
        colorBombBomb.explode(board,0,0,removes);
        assertTrue(removes.contains(new Point(2,2)));
        assertTrue(removes.contains(new Point(1,2)));
        assertTrue(removes.contains(new Point(3,2)));
    }

}
