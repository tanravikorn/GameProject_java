package logic.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class CrossExplosion extends ExplosionBase{

    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        affectedCandies.add(new Point(r,c));

        for(int col =0;col < board.getCols();col++){
            if(board.getCandy(r,col) != null) addAndTrigger(board,r,col,affectedCandies);
        }

        for(int row=0;row < board.getRows();row++){
            if(board.getCandy(row,c) != null) addAndTrigger(board,row,c,affectedCandies);
        }
    }
}
