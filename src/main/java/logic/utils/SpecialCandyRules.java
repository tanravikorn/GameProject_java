package logic.utils;

import logic.candy.Candy;
import logic.candy.CandyType;

import java.util.Set;

public class SpecialCandyRules {
    public static CandyType analyzeShape(Set<Candy> clusters){
        int size = clusters.size();
        if(size <  4) return CandyType.NORMAL;
        int minR = Integer.MAX_VALUE, maxR = Integer.MIN_VALUE;
        int minC = Integer.MAX_VALUE, maxC = Integer.MIN_VALUE;
        for(Candy c : clusters){
            if(c.getRow() < minR) minR = c.getRow();
            if(c.getRow() > maxR) maxR = c.getRow();
            if(c.getColumn() < minC) minC = c.getColumn();
            if(c.getColumn() > maxC) maxC = c.getColumn();
        }
        int height = maxR - minR + 1;
        int width = maxC - minC +  1;
        boolean isLine = (height == 1 || width == 1);

        if(isLine && size >= 5) return CandyType.COLOR_BOMB;
        if(!isLine && size >= 5) return  CandyType.BOMB;

        if(isLine && size == 4){
            return (width > height) ? CandyType.STRIPED_VER : CandyType.STRIPED_HOR;
        }
        return CandyType.NORMAL;
    }
}
