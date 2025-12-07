package logic.Item;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class BombItem extends Item{
    @Override
    public List<Point> use(Board board, GameMode mode){
        List<Point> affect = new ArrayList<>();
        List<Candy> candies = getRandomCandy(board, 2);
        for(Candy c : candies){
            c.setType(CandyType.BOMB);
            affect.add(new Point(c.getRow(),c.getColumn()));
        }
        return affect;
    }
}
