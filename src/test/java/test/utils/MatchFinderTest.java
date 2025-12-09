package test.utils;
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.utils.MatchFinder;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MatchFinderTest {
    private final int ROWS = 5;
    private final int COLS = 5;
    Board board;
    MatchFinder matchFinder;

    @BeforeEach
    void setup(){
        board = new Board(5,5);
        for(int r =0;r<ROWS;r++){
            for(int c =0;c < COLS;c++){
                Candy candy  = new Candy(r,c, CandyColor.NONE);
                board.setCandy(r,c,candy);
            }
        }
        matchFinder = new MatchFinder(board);
    }

    @Test
    void findAtTest(){
        Candy c1 = board.getCandy(0,0);
        Candy c2 = board.getCandy(0,1);
        Candy c3 = board.getCandy(0,2);
        c1.setColor(CandyColor.BLUE);
        c2.setColor(CandyColor.BLUE);
        c3.setColor(CandyColor.BLUE);
        List<Set<Candy>> match;
        match = matchFinder.findMatchAt(0,0);
        assertEquals(1,match.size());
        assertTrue(!match.isEmpty());
        Set<Candy> candies = match.get(0);
        assertEquals(3,candies.size());
        assertTrue(candies.contains(c1));
        assertTrue(candies.contains(c2));
        assertTrue(candies.contains(c3));
    }
    @Test
    void finaAllTest(){
        Candy c1 = board.getCandy(0,0);
        Candy c2 = board.getCandy(0,1);
        Candy c3 = board.getCandy(0,2);
        c1.setColor(CandyColor.BLUE);
        c2.setColor(CandyColor.BLUE);
        c3.setColor(CandyColor.BLUE);

        Candy c4 = board.getCandy(1,2);
        Candy c5 = board.getCandy(2,2);
        Candy c6 = board.getCandy(3,2);
        c4.setColor(CandyColor.GREEN);
        c5.setColor(CandyColor.GREEN);
        c6.setColor(CandyColor.GREEN);
        List<Set<Candy>> match;
        match = matchFinder.findAllMatches();
        assertTrue(!match.isEmpty());
        assertEquals(2, match.size());
    }

    @Test
    void mergeTest(){
        List<Set<Candy>> match = new ArrayList<>();
        Set<Candy> s1 = new HashSet<>();
        Candy c1 = board.getCandy(0,0);
        Candy c2 = board.getCandy(0,1);
        Candy c3 = board.getCandy(0,2);
        c1.setColor(CandyColor.BLUE); s1.add(c1);
        c2.setColor(CandyColor.BLUE); s1.add(c2);
        c3.setColor(CandyColor.BLUE); s1.add(c3);
        match.add(s1);
        Set<Candy> s2 = new HashSet<>();
        Candy c4 = board.getCandy(0,2);
        Candy c5 = board.getCandy(1,2);
        Candy c6 = board.getCandy(2,2);
        c4.setColor(CandyColor.BLUE); s2.add(c4);
        c5.setColor(CandyColor.BLUE); s2.add(c5);
        c6.setColor(CandyColor.BLUE); s2.add(c6);
        match.add(s2);
        List<Set<Candy>> result = matchFinder.mergeClusters(match);
        assertEquals(1, result.size());
        Set<Candy> candies = result.get(0);
        assertTrue(candies.contains(c4));
        assertTrue(candies.contains(c6));
        assertTrue(candies.contains(c1));
    }

    @Test
    void hasPotentialTest(){
        Candy c1 = board.getCandy(0,0);
        Candy c2 = board.getCandy(0,1);
        Candy c3 = board.getCandy(0,3);
        c1.setColor(CandyColor.BLUE);
        c2.setColor(CandyColor.BLUE);
        c3.setColor(CandyColor.BLUE);
        assertTrue(matchFinder.hasPotentialMatch());
        c1.setColor(CandyColor.GREEN);
        c2.setColor(CandyColor.RED);
        assertFalse(matchFinder.hasPotentialMatch());
    }


}
