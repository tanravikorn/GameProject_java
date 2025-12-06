package logic.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.utils.Point;

import java.util.Set;

public class NormalExplosion extends ExplosionBase{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        //must implement
        Candy target = board.getCandy(r,c);
        if(target != null ){
            if(target.isFrozen()){
                target.setFrozen(false);
                return;
            }
            affectedCandies.add(new Point(r,c));
        }

    }
}
