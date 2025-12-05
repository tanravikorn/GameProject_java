package logic.utils;

import java.util.Objects;

public class Point {
    public int r;
    public int c;
    public Point(int r, int c){
        this.r = r;
        this.c = c;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return r == point.r && c == point.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }
}
