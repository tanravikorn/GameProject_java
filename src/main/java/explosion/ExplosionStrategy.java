package explosion;

import logic.board.Board;
import logic.utils.Point;


import java.util.Set;

public interface ExplosionStrategy {
    void explode(Board board, int r, int c, Set<Point> affectedCandies);
}
