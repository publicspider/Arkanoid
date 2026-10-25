package src;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * The SpriteCollection class holds a collection of all sprite objects in the game.
 * It provides mechanisms to easily update all sprites' states and draw them on a surface.
 *
 * @author Your Name
 * @version 1.0
 */
public class SpriteCollection {
    private List<Sprite> sprites;

    /**
     * Initializes a new SpriteCollection with an empty list.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<>();
    }

    /**
     * Adds a new sprite to the collection.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }


    /**
     * Calls timePassed() on all sprites currently in the collection.
     */
    public void notifyAllTimePassed() {
        List<Sprite> spritesCopy = new ArrayList<>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.timePassed();
        }
    }

    /**
     * Calls drawOn(d) on all sprites currently in the collection.
     *
     * @param d the draw surface to draw on
     */
    public void drawAllOn(DrawSurface d) {
        List<Sprite> spritesCopy = new ArrayList<>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.drawOn(d);
        }
    }
}