import java.util.ArrayList;
import java.util.List;

/**
 * The GameEnvironment class manages and tracks all solid objects in the game 
 * that are capable of causing a collision (objects implementing the Collidable interface).
 * It provides the infrastructure to query trajectories, allowing moving objects like 
 * balls to scan the environment and detect upcoming impacts or intersection points.
 *
 * @author Koren Bar Yosef
 */
public class GameEnvironment {
    private List<Collidable> collidables;

    /**
     * Constructor to initialize an empty game environment.
     * Instantiates an internal ArrayList to store registered collidable elements.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<>();
    }

    /**
     * Adds a given collidable object to the game environment collection.
     *
     * @param c the Collidable element (e.g., Block, Paddle) to be registered
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }


    /**
     * Scans the environment to find the closest collision that is going to occur 
     * along a given movement trajectory line.
     * If the moving object will not collide with any registered collidable item 
     * within the trajectory range, it returns null. Otherwise, it compiles and 
     * returns a CollisionInfo object containing the closest intersection point 
     * relative to the start of the trajectory line and the object that was struck.
     *
     * @param trajectory the Line representing the vector path of a moving object 
     * from its current start point to its intended destination end point
     * @return a CollisionInfo instance holding the closest impact details, 
     * or null if no intersections are detected along the path
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        // If there are no physical objects registered in the collection, no collision is possible
        if (this.collidables.isEmpty()) {
            return null;
        }

        CollisionInfo closestCollision = null;
        double minDistance = Double.MAX_VALUE;

        // Iterate through all registered collidable items to test for geometry intersections
        for (Collidable c : this.collidables) {
            Rectangle rect = c.getCollisionRectangle();

            // Fetch the closest intersection point on this specific rectangle relative to the line origin
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);

            if (intersection != null) {
                // Measure the precise distance from the trajectory start to the intersection point
                double distance = trajectory.start().distance(intersection);

                // If this impact occurs sooner than previously checked impacts, update our closest tracking record
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCollision = new CollisionInfo(intersection, c);
                }
            }
        }

        return closestCollision;
    }
}