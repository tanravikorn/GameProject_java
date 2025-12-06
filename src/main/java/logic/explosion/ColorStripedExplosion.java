package logic.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.Point;

import java.util.Random;
import java.util.Set;

public class ColorStripedExplosion extends ExplosionBase{
    private CandyColor targetColor;
    private final Random random = new Random();

    public void setTargetColor(CandyColor color){
        this.targetColor = color;
    }
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        Candy base = board.getCandy(r,c);
        affectedCandies.add(new Point(r,c));

        int rows = board.getRows();
        int cols = board.getCols();
        for(int row = 0;row < rows;row++){
            for(int col =0;col < cols;col++){
                Candy target = board.getCandy(row,col);
                if(target != null && target != base && target.getColor() == targetColor &&
                    !target.isFrozen()){
                    if(random.nextBoolean()){
                        target.setType(CandyType.STRIPED_HOR);
                    }else{
                        target.setType(CandyType.STRIPED_VER);
                    }
                    target.performExplosion(board, affectedCandies);
                }
                if(target != null && target.isFrozen() && target.getColor() == this.targetColor){
                    target.setFrozen(false);
                }
            }
        }
    }

}
