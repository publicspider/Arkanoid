/**
 * The BouncingBallAnimation class serves as a standalone driver program that
 * launches a bouncing ball animation. It extracts the initial starting coordinates
 * and movement vectors from the command-line arguments provided at runtime.
 *
 * @author Koren Bar Yosef
 */
public class BouncingBallAnimation {

    /**
     * The main entry point of the application. It parses the command-line arguments
     * into integer values, constructs the ball's initial starting point, and delegates
     * the rendering and animation loop logic to the DrawAnimation utility class.
     *
     * @param args command-line arguments containing 4 elements in order:
     * args[0] - the initial X coordinate of the ball (int)
     * args[1] - the initial Y coordinate of the ball (int)
     * args[2] - the horizontal velocity step, dx (int)
     * args[3] - the vertical velocity step, dy (int)
     * @throws ArrayIndexOutOfBoundsException if fewer than 4 arguments are provided
     * @throws NumberFormatException          if any of the arguments cannot be parsed as integers
     */
    public static void main(String[] args) {
        // Parse the position coordinates from the runtime arguments
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);

        // Parse the velocity vector deltas from the runtime arguments
        int dx = Integer.parseInt(args[2]);
        int dy = Integer.parseInt(args[3]);

        // Construct the starting point geometry object
        Point start = new Point(x, y);

        // Execute the animation engine with the parsed parameters
        DrawAnimation.drawAnimation(start, dx, dy);
    }
}