package logic.board;

import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.MatchFinder;

import java.util.Random;

public class BoardInitializer {
    private static final Random random = new Random();

    public static void initialize(Board board, MatchFinder matchFinder, GameMode gameMode){
        do{
            fillBoardNoInitialMatches(board, gameMode);
        }while (!matchFinder.hasPotentialMatch());
    }
    private static void fillBoardNoInitialMatches(Board board, GameMode gameMode){
        int rows = board.getRows();
        int cols = board.getCols();
        int countIce = 0;
        for(int r =0;r < rows;r++){
            for(int c =0;c <cols;c++){
                CandyColor color;
                do{
                    color = getRandomColor();
                }while (isCreatingMatch(board,r,c,color));
                Candy newCandy = new Candy(r,c,color);
                if(gameMode == GameMode.HARD){
                    if (Math.random() < 0.25 && countIce <= 12) { // โอกาส 25%
                        newCandy.setFrozen(true);
                        countIce++;
                    }
                }
                board.setCandy(r,c, newCandy);
            }
        }
    }

    private static boolean isCreatingMatch(Board board, int r, int c, CandyColor color){
        if(c >= 2){
            Candy c1  = board.getCandy(r,c-1);
            Candy c2  = board.getCandy(r,c-2);
            if (c1 != null && c2 != null &&
                    c1.getColor() == color && c2.getColor() == color) {
                return true;
            }
        }
        if (r >= 2) {
            Candy c1 = board.getCandy(r - 1, c);
            Candy c2 = board.getCandy(r - 2, c);
            if (c1 != null && c2 != null &&
                    c1.getColor() == color && c2.getColor() == color) {
                return true;
            }
        }
        return false;

    }

    private static CandyColor getRandomColor() {
        CandyColor[] colors = {
                CandyColor.RED, CandyColor.GREEN, CandyColor.BLUE,
                CandyColor.YELLOW, CandyColor.PURPLE
        };
        return colors[random.nextInt(colors.length)];
    }
}
