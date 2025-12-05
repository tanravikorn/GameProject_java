package logic.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class NukeExplosion extends ExplosionBase{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        for(int row = 0;row < board.getRows(); row++){
            for(int col=0;col < board.getCols();col++){
                if(board.getCandy(row,col) != null){
                    affectedCandies.add(new Point(row,col));
                }
            }
        }
    }
}
