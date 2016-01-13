package sortAndSearch;

import sortAndSearch.Point;

import java.util.List;

public class Polygon {
    private List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getNext(int i) {
        return points.get((i + points.size() + 1) % points.size());
    }

    public Point getPrev(int i) {
        return points.get((i + points.size() - 1) % points.size());
    }

    public int getValue(int i) {
        int toLeft = points.get(i).compareTo(getPrev(i));
        int toRight = points.get(i).compareTo(getNext(i));
        if (toLeft != toRight) {
            return 0;
        }
        return toLeft > 0 ? 2 : -2;
    }

    public Point getPoint(int i) {
        return points.get(i);
    }

    public int getVerticesNumber() {
        return points.size();
    }
}