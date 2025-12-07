package logic.Item;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IceBreakItem extends Item{
    @Override
    public List<Point> use(Board board, GameMode mode){
        List<Point> affects  = new ArrayList<>();
        if(mode == GameMode.HARD){
            List<Candy> candies = new ArrayList<>();
            for(int r =0;r < board.getRows();r++){
                for(int c=0;c < board.getCols();c++){
                    Candy candy = board.getCandy(r,c);
                    if(candy != null && candy.isFrozen()){
                        candies.add(candy);
                    }
                }
            }
            Collections.shuffle(candies);
            int amount = Math.min(7, candies.size());
            for(int i =0;i < amount;i++){
                Candy candy = candies.get(i);
                candy.setFrozen(false);
                affects.add(new Point(candy.getRow(), candy.getColumn()));
            }
        }else{
            List<Candy> candies = getRandomCandy(board, 1);
            if(!candies.isEmpty()){
                Candy target = candies.get(0);
                target.setType(CandyType.COLOR_BOMB);
                affects.add(new Point(target.getRow(), target.getColumn()));
            }
        }
        return affects;
    }
}
