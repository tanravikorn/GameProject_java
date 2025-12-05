package logic.utils;


import logic.candy.Candy;
import logic.candy.CandyType;
import logic.explosion.*;

public class CandyMixer {
    public static ExplosionStrategy getComboExplosion(Candy c1, Candy c2){
        if (c1 == null || c2 == null) return null;

        CandyType t1 = c1.getType();
        CandyType t2 = c2.getType();

        boolean isStriped1 = isStriped(t1);
        boolean isStriped2 = isStriped(t2);
        boolean isBomb1 = (t1 == CandyType.BOMB);
        boolean isBomb2 = (t2 == CandyType.BOMB);
        boolean isColor1 = (t1 == CandyType.COLOR_BOMB);
        boolean isColor2 = (t2 == CandyType.COLOR_BOMB);
        if(isStriped1 && isStriped2) return new CrossExplosion();
        if ((isStriped1 && isBomb2) || (isBomb1 && isStriped2)) {
            Candy stripedCandy = isStriped1 ? c1 : c2;
            boolean isVer = (stripedCandy.getType() == CandyType.STRIPED_VER);
            return new LineExplosion(isVer, 1);
        }

        if (isBomb1 && isBomb2) return new AreaExplosion(2);

        if ((isColor1 && isStriped2) || (isStriped1 && isColor2)) {
            ColorStripedExplosion strategy = new ColorStripedExplosion();
            if (isStriped1) {
                strategy.setTargetColor(c1.getColor());
            } else {
                strategy.setTargetColor(c2.getColor());
            }
            return strategy;
        }
        if ((isColor1 && isBomb2) || (isBomb1 && isColor2)) {
            ColorBombBombExplosion strategy = new ColorBombBombExplosion();
            if (isBomb1) {
                strategy.setTargetColor(c1.getColor());
            } else {
                strategy.setTargetColor(c2.getColor());
            }
            return strategy;
        }
        if(isColor1 && isColor2) return new NukeExplosion();
        return null;
    }

    public static boolean isStriped(CandyType type){
        return type == CandyType.STRIPED_HOR || type == CandyType.STRIPED_VER;
    }

}
