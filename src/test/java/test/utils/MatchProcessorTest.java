package test.utils;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.MatchFinder;
import logic.utils.MatchProcessor;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MatchProcessorTest {
    private Board board;
    private final int ROWS = 5;
    private final int COLS = 5;
    private MatchFinder matchFinder;

    @BeforeEach
    void setup(){
        board = new Board(ROWS, COLS);
        for(int r =0;r < ROWS;r++){
            for(int c =0;c < COLS;c++){
                Candy candy = new Candy(r,c,CandyColor.NONE);
                board.setCandy(r,c,candy);
            }
        }
        matchFinder = new MatchFinder(board);
    }

    @Test
    void processMatchTest1(){
        List<Set<Candy>> match;
        for(int c =0;c< COLS;c++){
            Candy candy = board.getCandy(0,c);
            candy.setColor(CandyColor.BLUE);
        }
        match = matchFinder.findAllMatches();
        Set<Point> removes;
        removes = MatchProcessor.processMatches(board,match,board.getCandy(0,0));
        assertFalse(removes.contains(new Point(0,0)));
        assertTrue(board.getCandy(0,0).getType() == CandyType.COLOR_BOMB);
    }
    @Test
    void processMatchTest2(){
        List<Set<Candy>> match;
        for(int c =0;c< COLS-1;c++){
            Candy candy = board.getCandy(0,c);
            candy.setColor(CandyColor.BLUE);
        }
        match = matchFinder.findAllMatches();
        Set<Point> removes;
        removes = MatchProcessor.processMatches(board,match,board.getCandy(0,0));
        assertFalse(removes.contains(new Point(0,0)));
        assertTrue(board.getCandy(0,0).getType() == CandyType.STRIPED_VER);
    }
    @Test
    void processMatchTest3(){
        List<Set<Candy>> match;
        for(int c =0;c< COLS-2;c++){
            Candy candy = board.getCandy(0,c);
            candy.setColor(CandyColor.BLUE);
        }
        board.getCandy(0,0).setFrozen(true);
        match = matchFinder.findAllMatches();
        Set<Point> removes;
        removes = MatchProcessor.processMatches(board,match,null);
        assertFalse(removes.contains(new Point(0,0)));
        assertFalse(board.getCandy(0,0).isFrozen());
    }

}
