package test.board;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    Board board;
    @BeforeEach
    void setup(){
        board = new Board(5,6);
    }

    @Test
    void constructureTest(){
        assertEquals(5,board.getRows());
        assertEquals(6, board.getCols());
        for(int r = 0;r < 5;r++){
            for(int c =0; c < 6;c++){
                assertNull(board.getCandy(r,c));
            }
        }
    }
    @Test
    void addAndGetCandyTest(){
        Candy candy = new Candy(2,3, CandyColor.BLUE);

        board.setCandy(2,3, candy);
        assertTrue(board.getCandy(2,3) !=  null);
        assertTrue(board.getCandy(4,5) == null);

        assertNull(board.getCandy(99,99));
    }

    @Test
    void isValidTest(){
        assertTrue(board.isValid(1,4));
        assertTrue(board.isValid(2,5));
        assertFalse(board.isValid(4,7));
        assertFalse(board.isValid(6,6));
    }


}
