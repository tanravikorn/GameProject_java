package test.utils;

import logic.utils.Point;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    void constructureTest(){
        Point p1 = new Point(2,3);
        assertEquals(2,p1.r);
        assertEquals(3,p1.c);

        Point p2 = new Point(4,7);
        assertEquals(4,p2.r);
        assertEquals(7, p2.c);
    }
    @Test
    void hashAndEqualsTest(){
        Point p1 = new Point(4,5);
        Point p2 = new Point(4,5);
        Point p3 = new Point(3,2);

        assertTrue(p1.equals(p2));
        assertFalse(p1 == p3);

        Set<Point> removes = new HashSet<>();
        removes.add(p1);
        removes.add(p2);
        removes.add(p3);
        assertEquals(2, removes.size());
        assertTrue(removes.contains(p1));
        assertTrue(removes.contains(p3));
    }

}
