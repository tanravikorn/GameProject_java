package test.utils;
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.explosion.*;
import logic.utils.CandyMixer;
import logic.utils.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CandyMixerTest {
    private Candy c1;
    private Candy c2;
    @BeforeEach
    void setup(){
        c1 = new Candy(0,0,CandyColor.GREEN);
        c2 = new Candy(0,3,CandyColor.BLUE);
    }

    @Test
    void stripedMixTest(){
        c1.setType(CandyType.STRIPED_VER);
        c2.setType(CandyType.STRIPED_HOR);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof CrossExplosion);
    }
    @Test
    void colorStripedMixTest(){
        c1.setType(CandyType.COLOR_BOMB);
        c2.setType(CandyType.STRIPED_VER);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof ColorStripedExplosion);
    }
    @Test
    void colorBombMixTest(){
        c1.setType(CandyType.COLOR_BOMB);
        c2.setType(CandyType.BOMB);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof ColorBombBombExplosion);
    }
    @Test
    void bombBombMixTest(){
        c1.setType(CandyType.BOMB);
        c2.setType(CandyType.BOMB);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof AreaExplosion);
    }
    @Test
    void normalColorMixTest(){
        c2.setType(CandyType.COLOR_BOMB);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof ColorBombExplosion);
    }
    @Test
    void colorColorMixTest(){
        c1.setType(CandyType.COLOR_BOMB);
        c2.setType(CandyType.COLOR_BOMB);
        ExplosionStrategy result = CandyMixer.getComboExplosion(c1,c2);
        assertNotNull(result);
        assertTrue(result instanceof NukeExplosion);
    }
}
