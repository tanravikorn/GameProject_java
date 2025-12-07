package logic.Item;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.controller.GameMode;
import logic.utils.Point;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Item {
    protected final Random random = new Random();

    public abstract List<Point> use(Board board, GameMode mode);

    protected List<Candy> getRandomCandy(Board board, int count){
        List<Candy> candies = new ArrayList<>();
        int rows = board.getRows();
        int cols = board.getCols();

        for(int r =0;r < rows;r++){
            for(int c = 0;c < cols;c++){
                Candy candy = board.getCandy(r,c);
                if(candy != null && !candy.isFrozen() && candy.getType() == CandyType.NORMAL){
                    candies.add(candy);
                }
            }
        }

        List<Candy> result = new ArrayList<>();
        if(!candies.isEmpty()){
            Collections.shuffle(candies);
            int amount = Math.min(count, candies.size());
            for(int i=0;i < amount;i++){
                result.add(candies.get(i));
            }

        }
       return result;
    }

}
