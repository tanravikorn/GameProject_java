package test.gamecontroller;

import logic.Item.BombItem;
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;

import logic.controller.GameController;
import logic.controller.GameMode;
import logic.controller.GameState;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    private GameController controller;
    private final int ROWS = 9;
    private final int COLS = 9;

    @BeforeEach
    void setup(){
        controller = new GameController(ROWS,COLS, GameMode.NORMAL);

    }
    @Test
    void constructureTest(){
        assertEquals(20, controller.getMoveLeft());
        assertEquals(0, controller.getScore());
        assertEquals(GameState.PLAY, controller.getGameState());

        assertEquals(2, controller.getBombItemAmount());
        assertEquals(1, controller.getIceItemAmount());
    }

    @Test
    void swapTest(){
        Board board = controller.getBoard();

        Candy c1 = new Candy(0, 0, CandyColor.RED); board.setCandy(0,0,c1);
        Candy c2 = new Candy(0, 1, CandyColor.RED); board.setCandy(0,1,c2);
        Candy c3 = new Candy(0, 2, CandyColor.BLUE); board.setCandy(0,2,c3);
        Candy c4 = new Candy(0, 3, CandyColor.RED); board.setCandy(0,3,c4);

        Set<Point> removes = controller.handleSwap(0,2,0,3);
        assertFalse(removes.isEmpty());
        assertEquals(19, controller.getMoveLeft());
        assertTrue(controller.getScore() > 0);
    }

    @Test
    void invalidSwapTest(){
        Board board = controller.getBoard();
        Candy c1 = new Candy(0,0,CandyColor.BLUE);
        Candy c2 = new Candy(0,1,CandyColor.RED);
        board.setCandy(0,0,c1);
        board.setCandy(0,1,c2);

        Set<Point> removes = controller.handleSwap(0,0,0,1);
        assertTrue(removes.isEmpty());
        assertEquals(20, controller.getMoveLeft());
        assertEquals(0, controller.getScore());

        assertEquals(board.getCandy(0,0), c1);
    }

    @Test
    void frozenSwapTest(){
        Board board = controller.getBoard();
        Candy c1 = new Candy(0,0,CandyColor.RED);
        Candy c2 = new Candy(0,1,CandyColor.RED);
        c1.setFrozen(true);

        board.setCandy(0,0,c1);
        board.setCandy(0,1,c2);

        Set<Point> removes = controller.handleSwap(0,0,0,1);

        assertTrue(removes.isEmpty());
        assertEquals(c1, board.getCandy(0,0));
    }

    @Test
    void gameOverTest(){
        controller.setMoveLeft(1);
        Board board = controller.getBoard();
        board.setCandy(0, 0, new Candy(0, 0, CandyColor.RED));
        board.setCandy(0, 1, new Candy(0, 1, CandyColor.RED));
        board.setCandy(0, 2, new Candy(0, 2, CandyColor.BLUE));
        board.setCandy(0, 3, new Candy(0, 3, CandyColor.RED));
        controller.handleSwap(0, 2, 0, 3);
        assertEquals(0, controller.getMoveLeft());
        assertEquals(GameState.GAME_OVER, controller.getGameState());

    }
    @Test
    void testItemUsageAndInventory() {
        assertEquals(2, controller.getBombItemAmount());

        List<Point> affects = controller.applyItemTransform(new BombItem());

        assertEquals(1, controller.getBombItemAmount());
        assertNotNull(affects);
        controller.setReadyToPlay();
        controller.applyItemTransform(new BombItem());
        controller.setReadyToPlay();
        assertEquals(0, controller.getBombItemAmount());

        List<Point> affectsEmpty = controller.applyItemTransform(new BombItem());

        assertTrue(affectsEmpty.isEmpty());
        assertEquals(0, controller.getBombItemAmount());
    }
}
