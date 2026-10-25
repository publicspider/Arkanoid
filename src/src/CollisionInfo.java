package src;

/**
 * The CollisionInfo class stores details about a specific collision event
 * that occurs within the game environment. It acts as a data container,
 * holding the exact geometric point of impact and the specific collidable
 * object (such as a Block or a Paddle) that was hit.
 *
 * @author Koren Bar Yosef
 */
public class CollisionInfo {
    private Point collisionPoint;
    private Collidable collisionObject;

    /**
     * Constructor to create a new CollisionInfo instance containing the details
     * of a detected impact.
     *
     * @param collisionPoint  the exact geometric Point where the collision occurred
     * @param collisionObject the solid Collidable object involved in the collision
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    /**
     * Returns the exact point at which the collision occurred.
     *
     * @return the Point object representing the location of the impact
     */
    public Point collisionPoint() {
        return this.collisionPoint;
    }

    /**
     * Returns the collidable object involved in the collision.
     *
     * @return the Collidable instance representing the object that was struck
     */
    public Collidable collisionObject() {
        return this.collisionObject;
    }
}