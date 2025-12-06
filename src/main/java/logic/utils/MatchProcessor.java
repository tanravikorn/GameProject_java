package logic.utils;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchProcessor {
    public static Set<Point> processMatches(Board board, List<Set<Candy>> clusters, Candy swappedCandy){
        Set<Point> removes = new HashSet<>();
        for(Set<Candy> cluster : clusters){
            CandyType reward  = SpecialCandyRules.analyzeShape(cluster);
            if(reward != CandyType.NORMAL){
                Candy candyReward;
                if(swappedCandy != null && cluster.contains(swappedCandy)){
                    candyReward = swappedCandy;
                }else{
                    candyReward = cluster.iterator().next();
                }
                if(candyReward.getType() != CandyType.NORMAL){
                    candyReward.performExplosion(board,removes);
                    removes.remove(new Point(candyReward.getRow(), candyReward.getColumn()));
                }
                candyReward.setType(reward);
                for(Candy c : cluster){
                    if(!c.equals(candyReward)){
                        c.performExplosion(board, removes);
                    }
                }
            }else{
                for(Candy c : cluster){
                    c.performExplosion(board, removes);
                }
            }
        }
        return removes;
    }

}

