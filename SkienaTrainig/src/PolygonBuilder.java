import java.util.ArrayList;
import java.util.List;


public class PolygonBuilder {
    private List<Point> points;

    public PolygonBuilder addPoint(double x, double y) {
        if (points == null) {
            points = new ArrayList<>();
        }
        points.add(new Point(x, y));
        return this;
    }

    public Polygon build() {
        return new Polygon(points);
    }
}
