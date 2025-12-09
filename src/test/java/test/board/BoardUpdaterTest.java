package test.board;
import logic.board.Board;
import logic.board.BoardUpdater;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardUpdaterTest {
    private Board board;
    private final int rows = 5;
    private final int cols = 1;
    @BeforeEach
    void setup(){
        board = new Board(rows,cols);
        for(int r =0;r < rows;r++){
            Candy candy = new Candy(r,0,CandyColor.PURPLE);
            board.setCandy(r,0,candy);
        }
    }

    @Test
    void gravityTest(){
        Candy c0 = new Candy(0,0,CandyColor.GREEN);
        Candy c1 = new Candy(1,0,CandyColor.BLUE);
        Candy c2 = new Candy(2,0,CandyColor.RED);
        Set<Point> removes = new HashSet<>();
        removes.add(new Point(2,0));
        board.setCandy(0,0,c0);
        board.setCandy(1,0,c1);
        board.setCandy(2,0,c2);

        BoardUpdater.updateBoard(board,removes);

        assertEquals(c1, board.getCandy(2,0));
        assertEquals(c0, board.getCandy(1,0));
        assertNotNull(board.getCandy(0,0));
        assertNotEquals(c0, board.getCandy(0,0));
    }

    @Test
    void refillTest(){
        Set<Point> removes = new HashSet<>();
        for(int r =0;r < rows;r++){
            removes.add(new Point(r,0));
        }
        BoardUpdater.updateBoard(board,removes);
        for(int r =0;r < rows;r++){
            assertNotNull(board.getCandy(r,0));
        }
    }

    @Test
    void frozenGravityTest(){
        Candy c0 = new Candy(0,0,CandyColor.GREEN);
        Candy c1 = new Candy(1,0,CandyColor.BLUE);
        Candy c2 = new Candy(2,0,CandyColor.RED);
        c1.setFrozen(true);
        Set<Point> removes = new HashSet<>();
        removes.add(new Point(2,0));
        board.setCandy(0,0,c0);
        board.setCandy(1,0,c1);
        board.setCandy(2,0,c2);
        BoardUpdater.updateBoard(board,removes);

        assertEquals(c1, board.getCandy(1,0));
        assertEquals(c0, board.getCandy(0,0));
        assertNotNull(board.getCandy(2,0));
        assertNotEquals(c2, board.getCandy(2,0));
    }

}
