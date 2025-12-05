package logic.explosion;

import logic.board.Board;
import logic.utils.Point;

import java.util.Set;

public class BigCrossExplosion implements ExplosionStrategy{
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        for(int rowOffset = -1;rowOffset <=1; rowOffset++){
            int currentRow = r + rowOffset;
            if(currentRow >= 0 && currentRow < board.getRows()){
                for(int col = 0 ;col < board.getCols();col++){
                    if(board.getCandy(currentRow, col) != null){
                        affectedCandies.add(new Point(currentRow, col));
                    }
                }
            }
        }

        for(int colOffset = -1;colOffset <= 1; colOffset++){
            int currentCol = c + colOffset;
            if(currentCol >= 0 && currentCol < board.getCols()){
                for(int row = 0;row < board.getRows();row++){
                    if(board.getCandy(row,currentCol) != null){
                        affectedCandies.add(new Point(row,currentCol));
                    }
                }
            }
        }
    }

}
