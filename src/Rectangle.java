import java.util.ArrayList;
import java.util.List;

/**
 * The Rectangle class represents a 2D rectangle defined by its upper-left
 * corner point, width, and height. It provides methods to compute
 * intersection points with lines and retrieve its dimensions.
 *
 * @author Your Name
 * @version 1.0
 */
public class Rectangle {

    private Point upperLeft;
    private double width;
    private double height;

    /**
     * Constructs a new Rectangle with a specified upper-left corner,
     * width, and height.
     *
     * @param upperLeft the upper-left corner point of the rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     */
    public Rectangle(Point upperLeft, double width, double height){
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns a (possibly empty) List of intersection points between
     * this rectangle and the specified line.
     *
     * @param line the line to check for intersections with the rectangle
     * @return a List of intersection points, which can be empty if no intersections occur
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        List<Point> intersectionList = new ArrayList<>();

        double x = this.upperLeft.getX();
        double y = this.upperLeft.getY();

        Point topLeft = this.upperLeft;
        Point topRight = new Point(x + this.width, y);
        Point bottomLeft = new Point(x, y + this.height);
        Point bottomRight = new Point(x + this.width, y + this.height);

        Line topSide = new Line(topLeft, topRight);
        Line bottomSide = new Line(bottomLeft, bottomRight);
        Line leftSide = new Line(topLeft, bottomLeft);
        Line rightSide = new Line(topRight, bottomRight);

        Point intersectTop = line.intersectionWith(topSide);
        if (intersectTop != null) {
            intersectionList.add(intersectTop);
        }

        Point intersectBottom = line.intersectionWith(bottomSide);
        if (intersectBottom != null) {
            intersectionList.add(intersectBottom);
        }

        Point intersectLeft = line.intersectionWith(leftSide);
        if (intersectLeft != null) {
            intersectionList.add(intersectLeft);
        }

        Point intersectRight = line.intersectionWith(rightSide);
        if (intersectRight != null) {
            intersectionList.add(intersectRight);
        }

        return intersectionList;
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return the width of the rectangle
     */
    public double getWidth(){
        return width;
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return the height of the rectangle
     */
    public double getHeight(){
        return height;
    }

    /**
     * Returns the upper-left point of the rectangle.
     *
     * @return the upper-left corner {@link Point} of the rectangle
     */
    public Point getUpperLeft(){
        return upperLeft;
    }
}