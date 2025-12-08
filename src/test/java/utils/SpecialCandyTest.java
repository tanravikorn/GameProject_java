package utils;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.SpecialCandyRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class SpecialCandyTest {
    private Board board;
    private final int ROWS = 5;
    private final int COLS = 5;
    private Set<Candy> candies;

    @BeforeEach
    void setup(){
        board = new Board(ROWS,COLS);
        for (int r =0;r < ROWS;r++){
            for(int c =0;c <COLS;c++){
                Candy candy = new Candy(r,c,CandyColor.BLUE);
                board.setCandy(r,c,candy);
            }
        }
        candies = new HashSet<>();
    }

    @Test
    void normalCanyTest(){
        for(int r=0;r<ROWS-2;r++){
            candies.add(board.getCandy(r,0));
        }
        assertEquals(3,candies.size());
        assertEquals(CandyType.NORMAL, SpecialCandyRules.analyzeShape(candies));
    }
    @Test
    void colorCanyTest(){
        for(int r=0;r<ROWS;r++){
            candies.add(board.getCandy(r,0));
        }
        assertEquals(5,candies.size());
        assertEquals(CandyType.COLOR_BOMB, SpecialCandyRules.analyzeShape(candies));
    }
    @Test
    void lineCanyTest(){
        for (int r =0;r<ROWS-1;r++){
            candies.add(board.getCandy(r,0));
        }
        assertEquals(CandyType.STRIPED_HOR, SpecialCandyRules.analyzeShape(candies));
        candies.clear();
        for(int c =0;c < COLS-1;c++){
            candies.add(board.getCandy(0,c));
        }
        assertEquals(CandyType.STRIPED_VER, SpecialCandyRules.analyzeShape(candies));
    }

    @Test
    void bombCandyTest(){
        for (int r =0;r<ROWS-1;r++){
            candies.add(board.getCandy(r,0));
        }
        for(int c = 0;c < COLS - 3;c++){
            candies.add(board.getCandy(0,c));
        }
        assertEquals(CandyType.BOMB, SpecialCandyRules.analyzeShape(candies));

    }
}
