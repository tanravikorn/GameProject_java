package logic.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class BigBombExplosion implements ExplosionStrategy{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        for(int row = r-2;row <= r+2;row++){
            for(int col = c-2;col <= c+2;col++){
                if(board.isValid(row,col) && board.getCandy(row,col) != null){
                    affectedCandies.add(new Point(row,col));
                }
            }
        }
    }
}
