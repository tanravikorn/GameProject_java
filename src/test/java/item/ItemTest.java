package item;
import logic.Item.BombItem;
import logic.Item.IceBreakItem;
import logic.Item.Item;
import logic.Item.StripedItem;
import logic.board.Board;
import logic.board.BoardInitializer;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameMode;
import logic.explosion.*;
import logic.utils.CandyMixer;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    private Board board ;
    private final int ROWS = 5;
    private final int COLS = 5;
    private Item bombItem = new BombItem();
    private Item iceBreaker = new IceBreakItem();
    private Item stripedItem = new StripedItem();

    @BeforeEach
    void setup(){
        board = new Board(ROWS,COLS);
        for(int r =0;r<ROWS;r++){
            for(int c =0;c< COLS;c++){
                Candy candy = new Candy(r,c,CandyColor.RED);
                board.setCandy(r,c,candy);
            }
        }
    }

    @Test
    void bombItemTest(){
        List<Point> removes = bombItem.use(board, GameMode.NORMAL);
        assertFalse(removes.isEmpty());
    }
    @Test
    void stripedItem(){
        List<Point> removes = stripedItem.use(board, GameMode.NORMAL);
        assertFalse(removes.isEmpty());
    }
    @Test
    void iceBreakerNormalTest(){
        List<Point> color = iceBreaker.use(board, GameMode.NORMAL);
        assertFalse(color.isEmpty());
        Point p = color.get(0);
        assertEquals(CandyType.COLOR_BOMB, board.getCandy(p.r,p.c).getType());
    }
    @Test
    void iceBreakerHardTest(){
        Candy c1 = board.getCandy(0,0);
        Candy c2 = board.getCandy(0,1);
        c1.setFrozen(true);
        c2.setFrozen(true);

        List<Point> melt = iceBreaker.use(board, GameMode.HARD);
        assertEquals(2, melt.size());
        assertFalse(c1.isFrozen());
        assertFalse(c2.isFrozen());
    }
}
