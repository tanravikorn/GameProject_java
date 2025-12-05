package logic.utils;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;

import java.util.*;

public class MatchFinder {

    Board board;
    public MatchFinder(Board board){
        this.board = board;
    }
    //หาเฉพาะจุด
    public List<Set<Candy>> findMatchAt(int r, int c){
        List<Set<Candy>> result = new ArrayList<>();
        Candy baseCandy = board.getCandy(r,c);
        if(baseCandy == null || baseCandy.getColor() == CandyColor.NONE) return result;
        Set<Candy> clusters = new HashSet<>();
        CandyColor color = baseCandy.getColor();
        int rows = board.getRows();
        int cols = board.getCols();

        //Horzital
        int left = c;
        while(left > 0 && checkColor(r, left -1 , color)) left--;
        int right = c;
        while(right < cols-1 && checkColor(r, right+1, color)) right++;
        if(right - left + 1>=3){
            for(int k = left;k<=right;k++){
                clusters.add(board.getCandy(r,k));
            }
        }
        //Vertical
        int top = r;
        while(top > 0 && checkColor(top-1, c,color)) top--;
        int bottom = r;
        while (bottom < rows -1 && checkColor(bottom+1,c,color)) bottom++;
        if(bottom - top  + 1>=3){
            for(int k = top;k<=bottom;k++){
                clusters.add(board.getCandy(k,c));
            }
        }
        if(!clusters.isEmpty()){
            result.add(clusters);
        }
        return  result;

    }
    //ทุกจุดใน board
    public List<Set<Candy>> findAllMatches() {
        List<Set<Candy>> allClusters = new ArrayList<>();
        int rows = board.getRows();
        int cols = board.getCols();

        //Horizontal
        for (int r = 0; r < rows; r++) {
            List<Candy> currentRun = new ArrayList<>();
            for (int c = 0; c < cols; c++) {
                Candy current = board.getCandy(r, c);
                if(shouldEndRun(currentRun, current)){
                    if (currentRun.size() >= 3){
                        allClusters.add(new HashSet<>(currentRun));
                    }
                    currentRun.clear();
                }
                if(current != null && current.getColor() != CandyColor.NONE){
                    currentRun.add(current);
                }
            }
            if(currentRun.size() >= 3){
                allClusters.add(new HashSet<>(currentRun));
            }
        }
        //vertical
        for(int c =0;c <cols;c++){
            List<Candy> currentRun = new ArrayList<>();
            for (int r =0;r < rows;r++){
                Candy current = board.getCandy(r,c);
                if(shouldEndRun(currentRun, current)){
                    if(currentRun.size() >= 3){
                        allClusters.add(new HashSet<>(currentRun));
                    }
                    currentRun.clear();
                }
                if(current != null && current.getColor() != CandyColor.NONE){
                    currentRun.add(current);
                }
            }
            if(currentRun.size() >= 3){
                allClusters.add(new HashSet<>(currentRun));
            }
        }
        return mergeClusters(allClusters);
    }

    public List<Set<Candy>> mergeClusters(List<Set<Candy>> clusters){
        boolean merged = true;
        while(merged){
            merged = false;

            for(int i =0; i<clusters.size();i++){
                for(int j = i+1;j < clusters.size();j++){
                    Set<Candy> setA = clusters.get(i);
                    Set<Candy> setB = clusters.get(j);
                    if(!Collections.disjoint(setA,setB)){
                        setA.addAll(setB);
                        clusters.remove(j);
                        merged = true;
                        break;
                    }
                }
                if(merged) break;
            }
        }
        return clusters;
    }


    public boolean checkColor(int r, int c, CandyColor color){
        Candy candy = board.getCandy(r,c);
        return (candy != null) && (color == candy.getColor());
    }

    public boolean shouldEndRun(List<Candy> run, Candy current){
        if(run.isEmpty()) return false;
        if(current == null) return true;
        if(current.getColor() == CandyColor.NONE) return true;
        Candy last = run.get(run.size()-1);
        return last.getColor() != current.getColor();
    }
}
