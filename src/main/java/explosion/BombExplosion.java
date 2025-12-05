package explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class BombExplosion implements ExplosionStrategy{

    @Override
    public void explode(Board board, int r, int c, Set<Point> affectCandies){
        //must implement
        for(int row = r-1;row <= r+1;row++){
            for(int col = c -1;col <= c+1;col++){
                if(board.isValid(row,col) && board.getCandy(row,col) != null){
                    affectCandies.add(new Point(row,col));
                }
            }
        }
    }
}
