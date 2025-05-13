package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Represents an extra paddle in the Bricker game.
 * This paddle tracks the number of times it is hit by a ball or puck.
 * It extends the functionality of the base `bricker.gameobjects.Paddle` class.
 */
public class ExtraPaddle extends Paddle {
    private int hits;

    /**
     * Constructs a new bricker.gameobjects.ExtraPaddle instance.
     *
     * @param topLeftCorner    Position of the paddle in window coordinates (pixels).
     *                         The top-left corner of the window is at (0,0).
     * @param dimensions       The width and height of the paddle in window coordinates.
     * @param renderable       The renderable representation of the paddle. Can be null if no rendering is
     *                         needed.
     * @param inputListener    Listener for detecting user input (e.g., arrow keys for movement).
     * @param windowDimensions The dimensions of the game window, used to ensure the paddle stays within bounds.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.hits = 0;
    }

    /**
     * Handles the logic for when the extra paddle collides with another game object.
     * Increments the hit counter if the colliding object is tagged as "ball" or "puck".
     *
     * @param other     The game object that collided with the paddle.
     * @param collision Information about the collision, including the collision normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals("ball") || other.getTag().equals("puck")) {
            hits++;
        }
    }


    /**
     * Gets the number of times this paddle has been hit by a ball or puck.
     *
     * @return The hit count.
     */
    public int getHits() {
        return hits;
    }
}
