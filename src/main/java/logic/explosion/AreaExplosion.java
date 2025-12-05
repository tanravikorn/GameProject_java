package logic.explosion;

import logic.board.Board;
import logic.utils.Point;
import java.util.Set;

public class AreaExplosion extends ExplosionBase {

    private int radius; // 1 = 3x3, 2 = 5x5

    public AreaExplosion(int radius) {
        this.radius = radius;
    }

    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies) {
        for (int row = r - radius; row <= r + radius; row++) {
            for (int col = c - radius; col <= c + radius; col++) {
                if (board.isValid(row, col) && board.getCandy(row, col) != null) {
                    addAndTrigger(board,row,col,affectedCandies);
                }
            }
        }
    }
}