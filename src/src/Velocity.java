package src;

/**
 * Velocity specifies the change in position on the x and the y axes.
 */
public class Velocity {
    private double dx;
    private double dy;

    /**
     * Constructor for Velocity.
     *
     * @param dx the change in x position
     * @param dy the change in y position
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Creates a Velocity object from angle and speed.
     *
     * @param angle the angle in degrees
     * @param speed the speed of the movement
     * @return a new Velocity object
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.sin(radians);
        double dy = -speed * Math.cos(radians);
        return new Velocity(dx, dy);
    }

    /**
     * Returns the change in x-coordinate.
     *
     * @return the dx value
     */
    public double getDx() {
        return dx;
    }

    /**
     * Returns the change in y-coordinate.
     *
     * @return the dy value
     */
    public double getDy() {
        return dy;
    }

    /**
     * Take a point with position (x,y) and return a new point with position (x+dx, y+dy).
     *
     * @param p the original point
     * @return a new point after the movement
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }
}