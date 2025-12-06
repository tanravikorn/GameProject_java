package logic.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.utils.Point;

import java.util.Random;
import java.util.Set;

public class ColorBombExplosion extends ExplosionBase{
    private CandyColor targetColor = null;

    public void setTargetColor(CandyColor color){
        this.targetColor = color;
    }

    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies){
        affectedCandies.add(new Point(r,c));
        CandyColor colorToDestroy;
        if(targetColor != null){
            colorToDestroy = targetColor;
        }
        else {
            colorToDestroy = randomColor();
        }
        int rows = board.getRows();
        int cols = board.getCols();
        for(int row = 0;row < rows;row++){
            for(int col = 0;col < cols;col++){
                Candy target = board.getCandy(row,col);
                if(target != null && target.getColor() != CandyColor.NONE &&
                        target.getColor() == colorToDestroy && !target.isFrozen()){
                    addAndTrigger(board,row,col,affectedCandies);
                }else if(target != null && target.isFrozen() && target.getColor() == colorToDestroy){
                    target.setFrozen(false);
                }
            }
        }
        targetColor = null;
    }

    private CandyColor randomColor(){
        CandyColor[] colors = {CandyColor.BLUE, CandyColor.GREEN,CandyColor.RED,
                                CandyColor.YELLOW, CandyColor.PURPLE};
        return colors[new Random().nextInt(colors.length)];
    }
}
