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
}
