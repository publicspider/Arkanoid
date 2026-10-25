import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.*;

/**
 * The DrawAnimation class provides a utility infrastructure for execution of a simple,
 * standalone bouncing ball animation loop. It initializes its own graphical window
 * context, populates a single moving ball, and drives the frames sequencing sequentially.
 *
 * @author Koren Bar Yosef
 */
public class DrawAnimation {

    /**
     * Creates a graphical user interface context and runs a persistent animation
     * loop of a single ball, starting from a designated coordinate position
     * and propagating according to standard explicit differential vector velocity components.
     *
     * @param start the initial Point representing the geometric origin center location of the ball
     * @param dx    the initial horizontal translation step rate across the x-axis per frame
     * @param dy    the initial vertical translation step rate across the y-axis per frame
     */
    public static void drawAnimation(Point start, double dx, double dy) {
        // Instantiate an 800x600 GUI workspace frame and supporting utility instances
        GUI gui = new GUI("Bouncing Ball Animation", 800, 600);
        Sleeper sleeper = new Sleeper();
        GameEnvironment game = new GameEnvironment();

        // Instantiate a single black ball object pinned inside the dummy environment framework
        Ball ball = new Ball(start, 20, Color.BLACK, game);
        ball.setVelocity(dx, dy);

        // Persistent presentation rendering loop block
        while (true) {
            // Apply collision vector logic changes and progress structural center values
            ball.moveOneStep();

            // Acquire fresh back-buffer surface area context
            DrawSurface d = gui.getDrawSurface();

            // Paint current localized data matrix on surface
            ball.drawOn(d);

            // Swap standard display context layout onto visual rendering pane
            gui.show(d);

            // Maintain continuous animation pace by pausing execution flow thread for 50 milliseconds
            sleeper.sleepFor(50);
        }
    }
}