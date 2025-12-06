package logic.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.utils.Point;

import java.util.Set;

public abstract class ExplosionBase implements ExplosionStrategy {
    public void addAndTrigger(Board board, int r, int c, Set<Point> affectedCandies) {
        if (!board.isValid(r, c)) return;

        Candy target = board.getCandy(r, c);
        Point p = new Point(r, c);
        if (target == null || affectedCandies.contains(p)) {
            return;
        }
        if(target.isFrozen()){
            target.setFrozen(false);
            return;
        }
        affectedCandies.add(p);
        target.performExplosion(board, affectedCandies);
    }
}