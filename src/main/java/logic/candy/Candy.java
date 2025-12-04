package logic.candy;

import logic.board.Board;
import logic.utils.explosion.*;
import logic.utils.Point;
import java.util.List;
public class Candy {
    private int r; //row
    private int c; //column
    private boolean isRemove;
    private CandyColor color;
    private CandyType type;
    private ExplosionStrategy explosion;

    public Candy(int r, int c, CandyColor color){
        setColumn(c);
        setRow(r);
        setIsRemove(false);
        this.color = color;
        setType(CandyType.NORMAL);
    }

    public void setRow(int r){this.r = r;}
    public void setColumn(int c){this.c = c;}
    public void setIsRemove(boolean isRemove){this.isRemove = isRemove;}

    public void setType(CandyType type){
        this.type = type;
        switch (type){
            case BOMB -> this.explosion = new BombExplosion();
            case STRIPED_HOR -> this.explosion = new StripedHorExplosion();
            case STRIPED_VER -> this.explosion = new StripedVerExplosion();
            default -> this.explosion = new NormalExplosion();
        }
    }
    public void performExplosion(Board board, List<Point> affectCandies){
        if(explosion != null){
            explosion.explode(board, this.r, this.c, affectCandies);
        }
    }

    public int getRow(){return this.r;}
    public int getColumn(){return this.c;}
    public boolean isRemove(){return this.isRemove;}
    public CandyType getType(){return this.type;}
    public CandyColor getColor(){return this.color;}

}
