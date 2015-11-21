import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Let P be a simple, but not necessarily convex, polygon and q an arbitrary point not necessarily in P.
 *
 * Design an efficient algorithm to find a line segment originating from q that intersects the maximum number of edges
 * of P. In other words, if standing at point q, in what direction should you aim a gun so the bullet will go through
 * the largest number of walls. A bullet through a vertex of P gets credit for only one wall. An O(nlogn) algorithm is
 * possible.
 */
public class PolygonGunner {
    private Polygon poly;
    private Point center;

    public PolygonGunner(Polygon poly, double x, double y) {
        this.poly = poly;
        for (Point p : this.poly.getPoints()) {
            p.setX(p.x - x);
            p.setY(p.y - y);
        }
        center = new Point(x, y);
    }

    public AbstractMap.SimpleEntry<Point, Point> getBestDirection() {
        List<Point> sortedPoints = new ArrayList<>();
        for (int i = 0; i < poly.getVerticesNumber(); i++) {
            int value = poly.getValue(i);
            if (value != 0) {
                poly.getPoint(i).setDelta(value);
                sortedPoints.add(poly.getPoint(i));
            }
        }
        sortedPoints.sort(new PointSortComparator());
        int curValue = sortedPoints.get(0).delta;
        int maxValue = curValue;
        int maxLeftPoint = 0;
        for (int i = 1; i < sortedPoints.size(); i++) {
            curValue += sortedPoints.get(i).delta;
            if (maxValue < curValue) {
                maxValue = curValue;
                maxLeftPoint = i;
            }
        }
        Point start = new Point(sortedPoints.get(maxLeftPoint).x, sortedPoints.get(maxLeftPoint).y);
        start.setX(start.x + center.x);
        start.setY(start.y + center.y);
        Point fin = new Point(sortedPoints.get((maxLeftPoint + sortedPoints.size() + 1) % sortedPoints.size()).x,
                sortedPoints.get((maxLeftPoint + sortedPoints.size() + 1) % sortedPoints.size()).y);
        fin.setX(fin.x + center.x);
        fin.setY(fin.y + center.y);
        System.out.println("Between " + start.toString() + " and " + fin.toString());
        return new AbstractMap.SimpleEntry<>(start, fin);
    }

    private boolean isBetween(Point p, Point start, Point fin) {
        return p.compareTo(start) >= 0 && p.compareTo(fin) <= 0;
    }

    public int countEdgesBetween(Point p1, Point p2) {
        int amount = 0;
        for (int i = 0; i < poly.getVerticesNumber(); i++) {
            if (isBetween(poly.getPoint(i), p1, p2) ||
                    isBetween(poly.getNext(i), p1, p2) ||
                    isBetween(p1, poly.getPoint(i), poly.getNext(i)) ||
                    isBetween(p2, poly.getPoint(i), poly.getNext(i))) {
                amount++;
            }
        }
        return amount;
    }


    class PointSortComparator implements Comparator<Point> {
        private int compareInFirstQuarter(double x1, double y1, double x2, double y2) {
            return Double.compare(x1 * y2, x2 * y1);
        }

        @Override
        public int compare(Point p1, Point p2) {
            if (p1.getQuarter() != p2.getQuarter()) {
                return Integer.compare(p1.getQuarter(), p2.getQuarter());
            }

            int q = p1.getQuarter();
            if (q == 1) {
                return compareInFirstQuarter(p1.x, p1.y, p2.x, p2.y);
            } else if (q == 2) {
                return -compareInFirstQuarter(p1.x, -p1.y, p2.x, -p2.y);
            } else if (q == 3) {
                return compareInFirstQuarter(-p1.x, -p1.y, -p2.x, -p2.y);
            } else {
                return -compareInFirstQuarter(-p1.x, p1.y, -p2.x, p2.y);
            }
        }
    }


    public static void main(String[] args) {
        Polygon p = new PolygonBuilder()
                .addPoint(3, 3)
                .addPoint(4, 4)
                .addPoint(5, 2)
                .addPoint(4, 1)
                .addPoint(3, 1)
                .addPoint(1, 2)
                .addPoint(2, 6)
                .addPoint(5, 5)
                .addPoint(2, 4)
                .addPoint(2, 2)
                .addPoint(4, 2)
                .addPoint(4, 3)
                .build();
        PolygonGunner pGunner = new PolygonGunner(p, 3, 2.5);
        AbstractMap.SimpleEntry<Point, Point> angle = pGunner.getBestDirection();
        System.out.println(pGunner.countEdgesBetween(angle.getKey(), angle.getValue()));
    }
}