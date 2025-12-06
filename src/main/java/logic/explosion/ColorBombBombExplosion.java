package logic.explosion;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.utils.Point;

import java.util.Set;

public class ColorBombBombExplosion extends ExplosionBase {

    private CandyColor targetColor;

    public void setTargetColor(CandyColor color) {
        this.targetColor = color;
    }
    @Override
    public void explode(Board board, int r, int c, Set<Point> affectedCandies) {
        Candy base = board.getCandy(r,c);
        affectedCandies.add(new Point(r, c));
        int rows = board.getRows();
        int cols = board.getCols();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Candy target = board.getCandy(row, col);
                if (target != null && target != base && target.getColor() == this.targetColor &&
                    !target.isFrozen()) {
                    target.setType(CandyType.BOMB);
                    target.performExplosion(board, affectedCandies);
                }else if(target != null && target.isFrozen() && target.getColor() == this.targetColor){
                    target.setFrozen(false);
                }
            }
        }
    }
}
