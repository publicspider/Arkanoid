package src;

import biuoop.DrawSurface;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The Block class represents a rectangular obstacle or wall in the game.
 * It implements both the Collidable and Sprite interfaces, meaning it can
 * bounce objects (like balls) off its surfaces and render itself onto the screen.
 * Blocks are static elements by default but participate fully in the game loop.
 *
 * @author Koren Bar Yosef
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle rect;
    private Color color;
    List<HitListener> hitListeners;

    /**
     * Constructor to create a new Block with a specific shape and color.
     *
     * @param rect  the Rectangle geometry defining the block's position and size
     * @param color the Color used to fill the block's inner area
     */
    public Block(Rectangle rect, Color color) {
        this.rect = rect;
        this.color = color;
        this.hitListeners = new ArrayList<HitListener>();
    }
    public void addHitListener(HitListener hl){
        hitListeners.add(hl);
    }
    public void removeHitListener(HitListener hl){
        hitListeners.remove(hl);
    }

    // --- Sprite Interface Implementation ---
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }
    public boolean ballColorMatch(Ball ball){
        if(this.color.equals(ball.getColor())){
            return true;
        }
        return false;

    }
    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }



    /**
     * Draws the block on the given DrawSurface.
     * It fills the inner rectangle with the block's custom color and draws
     * a black outline around it to clearly distinguish adjacent blocks.
     *
     * @param d the drawing surface provided by the GUI engine
     */
    @Override
    public void drawOn(DrawSurface d) {
        int x = (int) this.rect.getUpperLeft().getX();
        int y = (int) this.rect.getUpperLeft().getY();
        int width = (int) this.rect.getWidth();
        int height = (int) this.rect.getHeight();

        // 1. Fill the block interior with its designated row color
        d.setColor(this.color);
        d.fillRectangle(x, y, width, height);

        // 2. Draw a black border around the block for clean grid visual separation
        d.setColor(Color.BLACK);
        d.drawRectangle(x, y, width, height);
    }

    /**
     * Notifies the block that a unit of time has passed.
     * Currently, blocks are static game elements, so this method performs no actions.
     */
    @Override
    public void timePassed() {
        // Static blocks do not change state over time in this assignment
    }

    // --- Collidable Interface Implementation ---

    /**
     * Returns the collision rectangle shape of this block.
     *
     * @return the Rectangle object representing the physical boundaries of the block
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * Calculates and returns the new velocity of an object after colliding with this block.
     * It checks which face of the rectangle was hit (vertical walls vs. horizontal walls)
     * and inverts the respective velocity component (dx or dy).
     * Uses an epsilon threshold to avoid floating-point comparison inaccuracies.
     *
     * @param collisionPoint  the exact Point where the collision occurred
     * @param currentVelocity the Velocity vector of the hitting object prior to impact
     * @return the updated Velocity vector after the bounce response
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double leftX = this.rect.getUpperLeft().getX();
        double rightX = leftX + this.rect.getWidth();
        double topY = this.rect.getUpperLeft().getY();
        double bottomY = topY + this.rect.getHeight();

        // Epsilon factor to safely handle double inaccuracies during edge detection
        double epsilon = 0.00001;

        // Check for vertical surface impact (left or right side hit) -> invert horizontal movement
        if (Math.abs(collisionPoint.getX() - leftX) < epsilon ||
                Math.abs(collisionPoint.getX() - rightX) < epsilon) {
            dx = -dx;
        }

        // Check for horizontal surface impact (top or bottom side hit) -> invert vertical movement
        if (Math.abs(collisionPoint.getY() - topY) < epsilon ||
                Math.abs(collisionPoint.getY() - bottomY) < epsilon) {
            dy = -dy;
        }
        if (!ballColorMatch(hitter)){
            this.notifyHit(hitter);
        }

        return new Velocity(dx, dy);
    }

    public Color getColor() {
        return color;
    }

    /**
     * Registers this block into the central Game manager.
     * Since a block is both solid and visible, it adds itself to both the
     * collidables list and the sprites collection.
     *
     * @param g the Game instance managing the current game state
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
}