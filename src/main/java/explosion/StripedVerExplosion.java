package explosion;

import logic.board.Board;
import logic.utils.Point;


import java.util.Set;

public class StripedVerExplosion implements ExplosionStrategy{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectCandies){
        //must implement
        affectCandies.add(new Point(r,c));
        for(int row = 0; row < board.getRows();row++){
            if(row != r && board.getCandy(row,c) != null){
                affectCandies.add(new Point(row, c));
            }
        }

    }
}
