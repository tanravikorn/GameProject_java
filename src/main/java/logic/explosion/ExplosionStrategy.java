package logic.explosion;

import logic.board.Board;

import java.awt.*;
import java.util.List;

public interface ExplosionStrategy {
    void explode(Board board, int r, int c, List<Point> affectedCndies);
}
