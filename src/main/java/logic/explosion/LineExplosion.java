package logic.explosion;

import logic.board.Board;
import logic.utils.Point;
import java.util.Set;

public class LineExplosion extends ExplosionBase {
    private boolean isVertical;
    private int blastRadius;

    public LineExplosion(boolean isVertical) {
        this(isVertical, 0);
    }

    public LineExplosion(boolean isVertical, int blastRadius) {
        this.isVertical = isVertical;
        this.blastRadius = blastRadius;
    }

    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies) {
        for (int offset = -blastRadius; offset <= blastRadius; offset++) {
            if (isVertical) {
                int currentCol = c + offset;
                if (currentCol >= 0 && currentCol < board.getCols()) {
                    for (int row = 0; row < board.getRows(); row++) {
                        if (board.getCandy(row, currentCol) != null) {
                            addAndTrigger(board,row,currentCol,affectedCandies);
                        }
                    }
                }
            } else {
                int currentRow = r + offset;
                if (currentRow >= 0 && currentRow < board.getRows()) {
                    for (int col = 0; col < board.getCols(); col++) {
                        if (board.getCandy(currentRow, col) != null) {
                            addAndTrigger(board,currentRow,col,affectedCandies);
                        }
                    }
                }
            }
        }
    }
}