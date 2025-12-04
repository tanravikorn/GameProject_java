package logic.utils;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchFinder {

    public static List<Candy> findAllMatches(Board board){
        Set<Candy> matchSet = new HashSet<>();
        int rows = board.getRows();
        int cols = board.getCols();

        //Horizontal
        for(int r =0;r < rows;r++){
            for(int c = 0; c < cols -2;c++){
                Candy c1 = board.getCandy(r,c);
                Candy c2 = board.getCandy(r,c+1);
                Candy c3 = board.getCandy(r,c+2);
                if(checkThree(c1,c2,c3)){
                    matchSet.add(c1);
                    matchSet.add(c2);
                    matchSet.add(c3);
                }
            }
        }

        //vertical
        for(int c = 0; c < cols;c++){
            for(int r = 0;r < rows -2;r++){
                Candy c1 = board.getCandy(r,c);
                Candy c2 = board.getCandy(r+1,c);
                Candy c3 = board.getCandy(r+2,c);
                if(checkThree(c1,c2,c3)){
                    matchSet.add(c1);
                    matchSet.add(c2);
                    matchSet.add(c3);
                }
            }
        }
        return new ArrayList<>(matchSet);
    }

    private static boolean checkThree(Candy c1, Candy c2, Candy c3){
        if(c1 == null || c2 == null || c3 == null) return false;
        if(c1.getColor() == CandyColor.NONE) return false;
        return c1.getColor() == c2.getColor() && (c2.getColor() == c3.getColor());
    }
}
