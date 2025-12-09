package test.board;


import logic.board.Board;
import logic.board.BoardInitializer;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.controller.GameMode;
import logic.utils.MatchFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardInitializerTest {
    Board board;
    MatchFinder matchFinder;
    @BeforeEach
    void setup(){
        board = new Board(9,9);
        matchFinder = new MatchFinder(board);
    }

    @Test
    void initializeTest(){
        BoardInitializer.initialize(board,matchFinder, GameMode.NORMAL);
        for(int r=0; r < 9;r++){
            for(int c = 0; c< 9;c++){
                Candy candy = board.getCandy(r,c);
                assertNotNull(candy);
                assertTrue(!candy.isFrozen());
                if(c>=2){
                    Candy candy1 = board.getCandy(r,c-1);
                    Candy candy2 = board.getCandy(r,c-2);
                    CandyColor color = candy.getColor();
                    boolean isMatch = (candy1 != null && candy2 != null &&
                                        candy1.getColor() == color && candy2.getColor() == color);
                    assertFalse(isMatch);
                }
                if(r>=2){
                    Candy candy1 = board.getCandy(r-1,c);
                    Candy candy2 = board.getCandy(r-2,c);
                    CandyColor color = candy.getColor();
                    boolean isMatch = (candy1 != null && candy2 != null &&
                            candy1.getColor() == color && candy2.getColor() == color);
                    assertFalse(isMatch);
                }
            }

        }
    }
    @Test
    void initializeHardMode(){
        BoardInitializer.initialize(board,matchFinder, GameMode.HARD);
        boolean foundIce = false;
        for(int r = 0;r< 9;r++){
            for(int c =0;c < 9;c++){
                Candy candy = board.getCandy(r,c);
                if(candy.isFrozen()){
                    foundIce = true;
                    break;
                }
            }
        }
        assertTrue(foundIce);
    }

    @Test
    void canMoveTest(){
        BoardInitializer.initialize(board,matchFinder, GameMode.NORMAL);
        assertTrue(matchFinder.hasPotentialMatch());

        BoardInitializer.initialize(board,matchFinder, GameMode.HARD);
        assertTrue(matchFinder.hasPotentialMatch());
    }
}
