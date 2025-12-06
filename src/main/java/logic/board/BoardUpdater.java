package logic.board;

import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.utils.Point;

import java.util.*;

public class BoardUpdater {

    private static final Random random = new Random();
    public static void updateBoard(Board board, Set<Point> markRemoves){
        for(Point p : markRemoves){
            if(board.isValid(p.r,p.c)){
                Candy c = board.getCandy(p.r,p.c);
                if(c != null){
                    c.setIsRemove(true);
                }
                board.setCandy(p.r, p.c,null);
            }
        }

        applyGravity(board);

        refillBoard(board);
    }

    private static void refillBoard(Board board){
        int rows = board.getRows();
        int cols = board.getCols();
        for(int c =0;c <cols;c++){
            for(int r =0;r <rows;r++){
                if(board.getCandy(r,c) == null){
                    Candy newCandy = generateRandomCandy(r,c);
                    board.setCandy(r,c,newCandy);
                }
            }
        }
    }
    private static Candy generateRandomCandy(int r, int c){
        CandyColor[] colors = {
                CandyColor.PURPLE,
                CandyColor.YELLOW,
                CandyColor.RED,
                CandyColor.BLUE,
                CandyColor.GREEN
        };
        CandyColor randomCandy = colors[random.nextInt(colors.length)];
        return new Candy(r,c, randomCandy);
    }

    public static void shuffleBoard(Board board){
        List<Candy> allCandies = new ArrayList<>();
        int rows = board.getRows();
        int cols = board.getCols();

        for(int r= 0;r<rows;r++){
            for(int c =0;c <cols;c++){
                Candy candy = board.getCandy(r,c);
                if(candy != null){
                    allCandies.add(candy);
                }
            }
        }
        Collections.shuffle(allCandies);
        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (index < allCandies.size()) {
                    Candy shuffledCandy = allCandies.get(index++);
                    board.setCandy(r, c, shuffledCandy);
                }
            }
        }
    }

    private static void applyGravity(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int col = 0; col < cols; col++) {
            applyGravityColumn(board,rows,col);
        }
    }

    private static void applyGravityColumn(Board board, int rows, int col) {
        int writeRow = rows - 1;
        for (int r = rows - 1; r >= 0; r--) {
            Candy current = board.getCandy(r, col);
            if (current != null && current.isFrozen()) {
                while (writeRow > r) {
                    board.setCandy(writeRow, col, null);
                    writeRow--;
                }
                writeRow = r - 1;
            }
            else if (current != null) {
                board.setCandy(writeRow, col, current);
                writeRow--;
            }
        }

        while (writeRow >= 0) {
            board.setCandy(writeRow, col, null);
            writeRow--;
        }
    }
}
