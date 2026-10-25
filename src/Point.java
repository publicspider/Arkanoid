/**
 * The Point class represents a point in a 2D coordinate system (x, y).
 */
public class Point {
    private double x;
    private double y;

    /**
     * Constructor for a Point.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param other the other point to measure distance to
     * @return the distance between the two points
     */
    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Compares this point to another point to see if they are equal.
     *
     * @param other the other point to compare with
     * @return true if the points have the same coordinates, false otherwise
     */
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Returns the x-coordinate of this point.
     *
     * @return the x value
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of this point.
     *
     * @return the y value
     */
    public double getY() {
        return this.y;
    }
}