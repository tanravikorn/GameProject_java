package logic.utils;

import logic.candy.Candy;
import logic.candy.CandyType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchProcessor {
    public static Set<Point> processMatches(List<Set<Candy>> clusters, Candy swappedCandy){
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

                candyReward.setType(reward);
                for(Candy c : cluster){
                    if(!c.equals(candyReward)){
                        removes.add(new Point(c.getRow(), c.getColumn()));
                    }
                }
            }else{
                for(Candy c : cluster){
                    removes.add(new Point(c.getRow(), c.getColumn()));
                }
            }
        }
        return removes;
    }
}
