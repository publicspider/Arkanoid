import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;

import java.awt.*;

/**
 * The Game class is the central engine of the Arkanoid game.
 * It holds and manages the game environment (collidables), the sprite collection,
 * the graphical user interface (GUI), and handles both the structural setup (initialization)
 * and the continuous 60 FPS animation execution loop (running phase).
 *
 * @author Koren Bar Yosef
 */
public class Game {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;
    private Sleeper sleeper;
    private KeyboardSensor keyboard;
    private Counter counter;

    /**
     * Constructor to initialize the central data structures and utility tools.
     * Creates empty collections for sprites and collidables, and instantiates the Sleeper.
     */
    public Game() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.sleeper = new Sleeper();
        this.counter = new Counter();
    }

    /**
     * Adds a solid collidable object into the core game environment tracker.
     *
     * @param c the Collidable element to register
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }
    void removeCollidable(Collidable c){
        this.environment.removeCollidable(c);
    }

    /**
     * Adds a visible sprite object into the unified sprite collection framework.
     *
     * @param s the Sprite element to register
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }
    void removeSprite(Sprite s){
        this.sprites.removeSprite(s);
    }

    /**
     * Initializes a new game session.
     * This method builds the main GUI screen layout, connects the keyboard sensor infrastructure,
     * builds the four outer perimeter boundary walls, spawns the user-controlled Paddle,
     * launches two independent active moving balls, and positions a tiered cascade pattern of
     * multicolored bricks dynamically.
     */
    public void initialize() {
        // Instantiate the primary display frame with standard 800x600 resolution boundaries
        this.gui = new GUI("Arkanoid - Ass5Game", 800, 600);
        this.keyboard = this.gui.getKeyboardSensor();
        PrintingHitListener printListener = new PrintingHitListener();
        BlockRemover blockRemover = new BlockRemover(this, counter);


        // 1. Construct the four outer boundary protective walls (thickness of 20 pixels)
        Block topWall = new Block(new Rectangle(new Point(0, 0), 800, 20), Color.GRAY);
        Block bottomWall = new Block(new Rectangle(new Point(0, 580), 800, 20), Color.GRAY);
        Block leftWall = new Block(new Rectangle(new Point(0, 20), 20, 560), Color.GRAY);
        Block rightWall = new Block(new Rectangle(new Point(780, 20), 20, 560), Color.GRAY);



        // Register the perimeter block units into both structural lists via the proxy hook
        topWall.addToGame(this);
        bottomWall.addToGame(this); // Acting floor shield preventing balls from escaping screen context
        leftWall.addToGame(this);
        rightWall.addToGame(this);

        // 2. Build and position the player-controlled interactive Paddle element
        Paddle paddle = new Paddle(new Rectangle(new Point(350, 560), 100, 20), Color.YELLOW, this.keyboard);
        paddle.addToGame(this);

        // 3. Populate two independent operational active moving balls
        Ball ball1 = new Ball(new Point(400, 400), 7, Color.WHITE, this.environment);
        ball1.setVelocity(Velocity.fromAngleAndSpeed(45, 6));
        ball1.addToGame(this);

        Ball ball2 = new Ball(new Point(350, 350), 7, Color.WHITE, this.environment);
        ball2.setVelocity(Velocity.fromAngleAndSpeed(315, 5));
        ball2.addToGame(this);

        // 4. Construct the structured cascading block grid array system (6 distinct rows)
        Color[] rowColors = {Color.GRAY, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, Color.GREEN};

        int blockWidth = 50;
        int blockHeight = 20;
        int startY = 100; // Vertical origin marker where the top layer line starts assembling

        // Iterate sequentially layer by layer down the interface matrix
        for (int i = 0; i < 6; i++) {
            Color currentRowColor = rowColors[i];

            // Linear layout size scales downward dynamically: 12, 11, 10, 9, 8, 7 blocks
            int blocksInRow = 12 - i;
            int currentY = startY + (i * blockHeight);

            // Populate the line track blocks working horizontally backwards from right to left
            for (int j = 0; j < blocksInRow; j++) {
                // Calculate position relative to the right bounding wall boundary edge (780)
                int currentX = 780 - ((j + 1) * blockWidth);

                Block block = new Block(new Rectangle(new Point(currentX, currentY), blockWidth, blockHeight),
                        currentRowColor);

                block.addHitListener(blockRemover);//add the listner to every block
                block.addToGame(this);
                this.counter.increase(1);
            }
        }
    }

    /**
     * Executes the main active animation presentation loop.
     * Drives continuous sequencing at a consistent rate of 60 frames per second (FPS).
     * Calculates processing time offsets dynamically per frame to determine precise sleep duration
     * intervals, ensuring fluid rendering performance.
     */
    public void run() {
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        // Permanent engine frame loop sequence execution block
        while (true) {
            // Capture the precise initial timestamp marker of the current operational frame
            long startTime = System.currentTimeMillis();

            // Fetch a fresh draw canvas space context
            DrawSurface d = this.gui.getDrawSurface();

            // Render a clean solid uniform blue background layer across canvas area
            d.setColor(Color.BLUE);
            d.fillRectangle(0, 0, 800, 600);

            // Trigger a single-pass rendering cascade across all registered items
            this.sprites.drawAllOn(d);

            // Push updated canvas buffer onto display interface container
            this.gui.show(d);

            // Broadcast positional tick state update notifications out to all items
            this.sprites.notifyAllTimePassed();

            // Compute execution time elapsed during the rendering and processing phase
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;

            // Rest execution sequence safely using the timing overhead balance margin
            if (milliSecondLeftToSleep > 0) {
                this.sleeper.sleepFor(milliSecondLeftToSleep);
            }
            if (this.counter != null && this.counter.getValue() == 0) {
                this.gui.close();
                return;
            }
        }
    }


}