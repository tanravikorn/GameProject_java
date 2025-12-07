package logic.Item;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class IceBreakItem extends Item{
    @Override
    public List<Point> use(Board board, GameMode mode){
        List<Point> affects  = new ArrayList<>();
        if(mode == GameMode.HARD){
            for(int r =0;r < board.getRows();r++){
                for(int c=0;c < board.getCols();c++){
                    Candy candy = board.getCandy(r,c);
                    if(candy != null && candy.isFrozen()){
                        candy.setFrozen(false);
                        affects.add(new Point(r,c));
                    }
                }
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
