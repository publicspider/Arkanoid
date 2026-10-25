import biuoop.DrawSurface;
import biuoop.GUI;

import java.awt.*;

/**
 * The BallsTest1 class is a basic execution program designed to verify
 * the instantiation, configuration, and rendering of the Ball class.
 * It opens a simple graphical user interface (GUI) window and draws three
 * distinct balls with different positions, radii, and colors.
 *
 * @author Koren Bar Yosef
 */
public class BallsTest1 {

    /**
     * The main entry point of the test program.
     * It sets up an 800x600 GUI window, extracts its drawing surface,
     * instantiates three static balls without a game environment, and
     * renders them onto the screen.
     *
     * @param args command-line arguments (not used in this test)
     */
    public static void main(String[] args) {
        // Create a GUI window with dimensions 800x600
        GUI gui = new GUI("Balls Test 1", 800, 600);
        DrawSurface d = gui.getDrawSurface();

        // Instantiate three balls with distinct locations, sizes, and colors
        // The 4th argument is set to null as no GameEnvironment is required for this static test
        Ball b1 = new Ball(new Point(100.0, 100.0), 30, Color.RED, null);
        Ball b2 = new Ball(new Point(100.0, 150.0), 10, Color.BLUE, null);
        Ball b3 = new Ball(new Point(80.0, 249.0), 50, Color.GREEN, null);

        // Draw each ball onto the provided surface
        b1.drawOn(d);
        b2.drawOn(d);
        b3.drawOn(d);

        // Display the surface on the GUI window
        gui.show(d);
    }
}