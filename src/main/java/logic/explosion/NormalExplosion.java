package logic.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class NormalExplosion extends ExplosionBase{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        //must implement
        affectedCandies.add(new Point(r,c));
    }
}
