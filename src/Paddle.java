import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

import java.awt.*;

/**
 * The Paddle class represents the user-controlled player bar in the Arkanoid game.
 * It implements both the Sprite and Collidable interfaces, allowing it to respond
 * to keyboard input ticks, render itself onto the screen, and participate in complex
 * directional bouncing mechanics when hit by a ball.
 * This class includes a smooth screen-wrapping mechanism for boundary crossings.
 *
 * @author Koren Bar Yosef
 * @version 1.0
 */
public class Paddle implements Sprite, Collidable {

    /** The keyboard sensor device to read input control from the user. */
    private KeyboardSensor keyboard;

    /** The Rectangle geometry defining the paddle's position, width, and height. */
    private Rectangle rect;

    /** The interior fill color of the paddle. */
    private Color color;

    /** The movement translation step rate of the paddle in pixels per frame. */
    private double speed;

    /**
     * Constructor to create a new user-controlled Paddle instance.
     *
     * @param rect     the Rectangle geometry defining the paddle's starting size and position
     * @param color    the Color used to render the interior of the paddle
     * @param keyboard the KeyboardSensor provided by the GUI engine to track left/right arrows
     */
    public Paddle(Rectangle rect, Color color, KeyboardSensor keyboard) {
        this.rect = rect;
        this.color = color;
        this.keyboard = keyboard;
        this.speed = 7; // Balanced translation step rate per operational animation frame
    }

    /**
     * Moves the paddle left by its predefined speed.
     * Implements a smooth screen-wrap boundary policy: if the paddle completely
     * exits beyond the left side wall boundary (X = 20), its position wraps
     * around to seamlessly emerge from the right side boundary wall (X = 780).
     */
    public void moveLeft() {
        double currentX = this.rect.getUpperLeft().getX();
        double newX = currentX - this.speed;
        double paddleWidth = this.rect.getWidth();

        // Left boundary edge collision check against the 20-pixel perimeter wall
        if (newX < 20 - paddleWidth) {
            // Completed wrapping cycle -> snap position to flush against the right wall border
            newX = 780 - paddleWidth;
        }

        this.rect = new Rectangle(new Point(newX, this.rect.getUpperLeft().getY()),
                paddleWidth, this.rect.getHeight());
    }

    /**
     * Moves the paddle right by its predefined speed.
     * Implements a smooth screen-wrap boundary policy: if the paddle completely
     * exits beyond the right side wall boundary (X = 780), its position wraps
     * around to seamlessly emerge from the left side boundary wall (X = 20).
     */
    public void moveRight() {
        double currentX = this.rect.getUpperLeft().getX();
        double newX = currentX + this.speed;
        double paddleWidth = this.rect.getWidth();

        // Right boundary edge collision check against the 780-pixel perimeter wall
        if (newX > 780) {
            // Completed wrapping cycle -> snap position to flush against the left wall border
            newX = 20;
        }

        this.rect = new Rectangle(new Point(newX, this.rect.getUpperLeft().getY()),
                paddleWidth, this.rect.getHeight());
    }

    // --- Sprite Interface Implementation ---

    /**
     * Polling routine triggered on every system clock tick.
     * Queries the linked KeyboardSensor device to determine if the user is actively
     * pressing control arrows, and invokes the appropriate movement methods.
     */
    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft();
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight();
        }
    }

    /**
     * Renders the paddle onto the provided DrawSurface.
     * In addition to drawing the main paddle structure, this method evaluates if the paddle
     * is currently intersecting a screen boundary, in which case it renders a synchronized
     * "ghost copy" on the opposite side of the screen to achieve a fluid wrapping visual effect.
     *
     * @param d the drawing surface provided by the GUI engine
     */
    @Override
    public void drawOn(DrawSurface d) {
        int x = (int) this.rect.getUpperLeft().getX();
        int y = (int) this.rect.getUpperLeft().getY();
        int width = (int) this.rect.getWidth();
        int height = (int) this.rect.getHeight();

        // 1. Render primary paddle geometry frame at its current spatial location
        d.setColor(this.color);
        d.fillRectangle(x, y, width, height);
        d.setColor(Color.BLACK);
        d.drawRectangle(x, y, width, height);

        // Net playable field width background offset threshold: 780 - 20 = 760 pixels
        int playFieldWidth = 760;

        // 2. Wrap-around rendering: check if part of the paddle crossed the left boundary wall
        if (x < 20) {
            int ghostX = x + playFieldWidth;
            d.setColor(this.color);
            d.fillRectangle(ghostX, y, width, height);
            d.setColor(Color.BLACK);
            d.drawRectangle(ghostX, y, width, height);
        }

        // 3. Wrap-around rendering: check if part of the paddle crossed the right boundary wall
        else if (x + width > 780) {
            int ghostX = x - playFieldWidth;
            d.setColor(this.color);
            d.fillRectangle(ghostX, y, width, height);
            d.setColor(Color.BLACK);
            d.drawRectangle(ghostX, y, width, height);
        }
    }

    // --- Collidable Interface Implementation ---

    /**
     * Returns the collision shape representation of this paddle.
     *
     * @return the Rectangle object representing the physical boundary area of the paddle
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * Calculates the ball rebound velocity response following a structural surface impact.
     * The method divides the paddle horizontally into 5 equal regions. Depending on the exact
     * relative point of impact, it modulates the reflection angle to grant the player steering control:
     * Region 1 (far left) -> sharp left angle bounce (300 degrees)
     * Region 2 (mid left) -> moderate left angle bounce (330 degrees)
     * Region 3 (center)   -> standard block bounce (reverses vertical velocity component only)
     * Region 4 (mid right)-> moderate right angle bounce (30 degrees)
     * Region 5 (far right)-> sharp right angle bounce (60 degrees)
     * Conserves the ball's overall scalar speed throughout the vector angle transformation.
     *
     * @param collisionPoint  the exact Point where the ball struck the paddle face
     * @param currentVelocity the Velocity vector of the incoming ball prior to impact
     * @return the modified Velocity vector calculated after the parameterized surface reflection
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double rectX = this.rect.getUpperLeft().getX();
        double paddleWidth = this.rect.getWidth();

        // Calculate the absolute relative offset horizontal position of the impact point across the paddle
        double relativeHitX = collisionPoint.getX() - rectX;

        // Determine the horizontal width step per region slice segment
        double regionWidth = paddleWidth / 5.0;

        // Extract velocity deltas and preserve the absolute linear magnitude speed scalar
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        double speed = Math.sqrt(dx * dx + dy * dy);

        // Region 1: Far left edge impact -> sharp left rebound deflection vector
        if (relativeHitX < regionWidth) {
            return Velocity.fromAngleAndSpeed(300, speed);
        }
        // Region 2: Mid left edge impact -> moderate left rebound deflection vector
        else if (relativeHitX < regionWidth * 2) {
            return Velocity.fromAngleAndSpeed(330, speed);
        }
        // Region 3: Central target sector impact -> normal vertical bounce inversion behavior
        else if (relativeHitX < regionWidth * 3) {
            return new Velocity(dx, -dy);
        }
        // Region 4: Mid right edge impact -> moderate right rebound deflection vector
        else if (relativeHitX < regionWidth * 4) {
            return Velocity.fromAngleAndSpeed(30, speed);
        }
        // Region 5: Far right edge impact -> sharp right rebound deflection vector
        else {
            return Velocity.fromAngleAndSpeed(60, speed);
        }
    }

    /**
     * Helper routine to register this paddle instance securely into the primary Game manager.
     * Hooks up the instance as both an active visible screen element and an operational solid obstacle.
     *
     * @param g the Game manager coordinator pipeline instance reference
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}