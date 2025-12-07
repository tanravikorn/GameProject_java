package logic.Item;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class StripedItem extends Item{
    public List<Point> use(Board board, GameMode mode){
        List<Point> affects = new ArrayList<>();
        List<Candy> candies = getRandomCandy(board,2);
        for(Candy c : candies){
            c.setType(random.nextBoolean() ? CandyType.STRIPED_VER : CandyType.STRIPED_HOR);
            affects.add(new Point(c.getRow(),c.getColumn()));
        }
        return affects;
    }
}
