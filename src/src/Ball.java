package src;

import biuoop.DrawSurface;

import java.awt.*;

/**
 * The Ball class represents a 2D moving ball in the game.
 * It implements the Sprite interface, meaning it can be drawn on the screen
 * and its state changes over time. The ball tracks its center position, radius,
 * color, velocity, and its surrounding game environment to handle collisions.
 *
 * @author Koren Bar Yosef
 */
public class Ball implements Sprite {
    private Point center;
    private int r;
    private Color color;
    private Velocity velocity;
    private int minX, minY, maxX, maxY;
    private GameEnvironment gameEnvironment;

    /**
     * Constructor for a Ball with a specific game environment.
     * Initializes the ball's location, radius, color, and links it to the environment.
     * Default movement boundaries are set to 800x600.
     *
     * @param center          the center point of the ball
     * @param r               the radius of the ball
     * @param color           the color of the ball
     * @param gameEnvironment the environment containing all collidable objects
     */
    public Ball(Point center, int r, Color color, GameEnvironment gameEnvironment) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
        this.minX = 0;
        this.minY = 0;
        this.maxX = 800;
        this.maxY = 600;
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Sets the boundaries for the ball's movement frame.
     *
     * @param minX the minimum x-coordinate of the frame
     * @param minY the minimum y-coordinate of the frame
     * @param maxX the maximum x-coordinate of the frame
     * @param maxY the maximum y-coordinate of the frame
     */
    public void setFrame(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Returns the rounded x-coordinate of the ball's center.
     *
     * @return the x value of the center as an integer
     */
    public int getX() {
        return (int) this.center.getX();
    }

    /**
     * Returns the rounded y-coordinate of the ball's center.
     *
     * @return the y value of the center as an integer
     */
    public int getY() {
        return (int) this.center.getY();
    }

    /**
     * Returns the radius (size) of the ball.
     *
     * @return the radius of the ball
     */
    public int getSize() {
        return this.r;
    }

    /**
     * Returns the color of the ball.
     *
     * @return the color object of the ball
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Draws the ball and its black outline on the given DrawSurface.
     * This method overrides the Sprite interface definition.
     *
     * @param d the draw surface to draw the ball on
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Drawing the filled circle using the ball's color
        d.setColor(this.color);
        d.fillCircle((int) this.center.getX(), (int) this.center.getY(), this.r);

        // Drawing a black outline around the ball for better visibility
        d.setColor(Color.BLACK);
        d.drawCircle((int) this.center.getX(), (int) this.center.getY(), this.r);
    }

    /**
     * Notifies the ball that a unit of time has passed.
     * As a response, the ball moves one step forward using its collision logic.
     * This method overrides the Sprite interface definition.
     */
    @Override
    public void timePassed() {
        this.moveOneStep();
    }

    /**
     * Adds the ball to the given Game instance as a sprite object.
     *
     * @param g the game object to register the ball into
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the velocity of the ball using a Velocity object.
     *
     * @param v the new velocity object
     */
    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    /**
     * Sets the velocity of the ball using explicit dx and dy delta values.
     *
     * @param dx the change rate in the x-axis
     * @param dy the change rate in the y-axis
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Returns the current velocity of the ball.
     *
     * @return the velocity object of the ball
     */
    public Velocity getVelocity() {
        return this.velocity;
    }

    /**
     * Returns the exact center point of the ball.
     *
     * @return the center point object
     */
    public Point getCenter() {
        return this.center;
    }

    /**
     * Moves the ball one step forward according to its velocity and the objects
     * inside the game environment. If a collision is detected along its trajectory,
     * the ball moves nearly to the collision point and changes its direction.
     */
    public void moveOneStep() {
        // 1. Calculate the theoretical next position and create a trajectory line
        Point nextActualPoint = this.getVelocity().applyToPoint(this.center);
        Line trajectory = new Line(this.center, nextActualPoint);

        // 2. Check the game environment for the closest collision along the trajectory
        CollisionInfo info = this.gameEnvironment.getClosestCollision(trajectory);

        // 2.1 If no collision occurs, move directly to the next point
        if (info == null) {
            this.center = nextActualPoint;
        } else {
            // 2.2 Collision detected!
            Point collisionPoint = info.collisionPoint();

            // Slightly step back from the collision face to prevent overlapping/getting stuck
            double almostX = collisionPoint.getX() - Math.signum(this.velocity.getDx()) * 0.01;
            double almostY = collisionPoint.getY() - Math.signum(this.velocity.getDy()) * 0.01;

            // Relocate the ball center safely outside the collidable object
            this.center = new Point(almostX, almostY);

            // Update the ball's velocity vector based on the hit surface
            this.velocity = info.collisionObject().hit(this, collisionPoint, this.velocity);
        }

    }

}