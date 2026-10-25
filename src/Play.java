/**
 * The Play class serves as the main entry point for the Arkanoid game application.
 * It is responsible for instantiating the game engine, triggering the setup
 * of game elements, and starting the main animation loop.
 *
 * @author Koren Bar Yosef
 */
public class Play {

    /**
     * The main method that starts the execution of the program.
     * It creates a new Game object, initializes its environment, walls, blocks,
     * paddle, and balls, and then starts the 60 FPS animation loop.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Create a new instance of the game engine
        Game game = new Game();

        // Setup the GUI, keyboard sensors, game layout, blocks, paddle, and balls
        game.initialize();

        // Start the infinite animation loop to run the game
        game.run();
    }
}