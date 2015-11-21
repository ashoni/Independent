public class Point implements Comparable<Point> {
    public double x;
    public double y;
    public int delta = -100;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getQuarter() {
        if (x >= 0 && y >= 0) {
            return 1;
        } else if (x >= 0 && y < 0) {
            return 2;
        } else if (y < 0) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public int compareTo(Point p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return Double.compare(this.x * p.y, p.x * this.y);
    }


    @Override
    public String toString() {
        return String.format("{%.2f, %.2f}", x, y);
    }
}

