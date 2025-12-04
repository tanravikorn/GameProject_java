package logic.utils.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.List;

public interface ExplosionStrategy {
    void explode(Board board, int r, int c, List<Point> affectedCndies);
}
