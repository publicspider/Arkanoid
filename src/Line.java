import java.util.List;

/**
 * The Line class represents a 2D geometric line segment defined by two boundary
 * coordinates: a start point and an end point. It provides mathematical utility 
 * infrastructure to compute segment intersections, query spatial alignment, and 
 * detect the closest impact intersections relative to a given bounding rectangle.
 *
 * @author Koren Bar Yosef
 */
public class Line {
    private Point startPoint;
    private Point endPoint;

    /**
     * Constructor to create a new Line segment from two distinct points.
     *
     * @param startPoint the starting Point boundary of the line segment
     * @param endPoint   the ending Point boundary of the line segment
     */
    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * Returns the starting point of the line segment.
     *
     * @return the start Point object
     */
    public Point start() {
        return this.startPoint;
    }

    /**
     * Returns the ending point of the line segment.
     *
     * @return the end Point object
     */
    public Point end() {
        return this.endPoint;
    }

    /**
     * Calculates the intersection point between this line segment and another line segment.
     * The method uses a determinant-based algorithm to verify if lines are parallel 
     * or collinear using an epsilon threshold. If an algebraic intersection exists, 
     * it evaluates the scalar parameters (t and u) to ensure the intersection point lies 
     * within the strict finite boundaries of both segments [0, 1] rather than their 
     * infinite theoretical projections.
     *
     * @param other the other Line segment to check intersection with
     * @return the exact Point of intersection if it lies within both bounded segments, 
     * or null if they are parallel, collinear, or do not intersect
     */
    public Point intersectionWith(Line other) {
        // First line coordinate vectors (this)
        double x1 = this.startPoint.getX();
        double y1 = this.startPoint.getY();
        double x2 = this.endPoint.getX();
        double y2 = this.endPoint.getY();

        // Second line coordinate vectors (other)
        double x3 = other.start().getX();
        double y3 = other.start().getY();
        double x4 = other.end().getX();
        double y4 = other.end().getY();

        // Calculate the cross-product denominator determinant
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // If the denominator is nearly 0, lines are parallel or collinear -> no single intersection point
        if (Math.abs(denominator) < 0.000001) {
            return null;
        }

        // Calculate linear interpolation scalars for intersection tracking
        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator;

        // Scalars must be bounded within 0.0 and 1.0 inclusive to register inside both segments
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            double intersectX = x1 + t * (x2 - x1);
            double intersectY = y1 + t * (y2 - y1);
            return new Point(intersectX, intersectY);
        }

        // The intersection point falls outside the finite segment boundaries
        return null;
    }

    /**
     * Finds and returns the closest intersection point to the start of this line 
     * segment out of all intersection points with a given bounding rectangle.
     * If this line segment does not intersect with the given rectangle's boundary 
     * edges at all, it returns null.
     *
     * @param rect the Rectangle object whose four side segments are being tested
     * @return the Point of intersection closest to this line's start point, 
     * or null if no intersections occur
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        // Query the rectangle to collect all intersection points with this trajectory line
        List<Point> intersections = rect.intersectionPoints(this);

        // If the collection is empty, there are no intersection points present
        if (intersections.isEmpty()) {
            return null;
        }

        // Initialize minimum distance tracking with the first discovered intersection point
        Point closestPoint = intersections.get(0);
        double minDistance = this.startPoint.distance(closestPoint);

        // Scan through all captured intersections using a for-each loop to calculate the absolute minimum distance
        for (Point p : intersections) {
            double currentDistance = this.startPoint.distance(p);

            // If a closer intersection point is discovered, update tracking variables
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestPoint = p;
            }
        }

        return closestPoint;
    }
}