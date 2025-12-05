package logic.explosion;

import logic.board.Board;
import logic.utils.Point;


import java.util.Set;

public class StripedHorExplosion implements ExplosionStrategy{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectCandies){
        //must implement
        affectCandies.add(new Point(r,c));
        for(int cols = 0; cols < board.getCols();cols++){
            if(cols != c && board.getCandy(r,cols) != null){
                affectCandies.add(new Point(r, cols));
            }
        }
    }
}
