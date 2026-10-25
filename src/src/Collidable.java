package src;

/**
 * The Collidable interface defines the contract for all solid objects 
 * in the game environment that can detect and respond to collisions.
 * Any class implementing this interface must provide its geometric shape 
 * and calculate the resulting velocity vector when an object impacts its surface.
 *
 * @author Koren Bar Yosef
 */
public interface Collidable {

    /**
     * Returns the physical "collision shape" of the object.
     * The game environment uses this rectangular geometry to detect intersections 
     * and calculate the exact point of impact with the ball's trajectory line.
     *
     * @return the Rectangle object representing the boundary of the collidable element
     */
    Rectangle getCollisionRectangle();

    /**
     * Notifies the collidable object that an impact occurred on its surface,
     * and calculates the new velocity vector expected after the hit.
     * The bounce logic is determined by the object based on which surface face 
     * was struck (e.g., inverting horizontal or vertical velocity, or applying 
     * special angle-based rebounds for paddles).
     *
     * @param collisionPoint  the exact Point where the collision was detected
     * @param currentVelocity the Velocity vector of the colliding object prior to impact
     * @return the updated Velocity vector expected after the bounce response
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}