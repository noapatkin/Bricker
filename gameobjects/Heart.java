package bricker.gameobjects;

import bricker.main.HealthUI;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Represents a heart in the Bricker game.
 * A heart interacts with the paddle, and upon collision, it triggers a flag to add a new heart to the
 * health UI.
 * This class also maintains a static reference to the last collided heart to manage its state.
 */
public class Heart extends GameObject {
    private static Heart collisionHeartDelete;

    /**
     * Constructs a new bricker.gameobjects.Heart instance.
     *
     * @param topLeftCorner Position of the heart in window coordinates (pixels).
     *                      The top-left corner of the window is at (0,0).
     * @param dimensions    The width and height of the heart in window coordinates.
     * @param renderable    The renderable representation of the heart. Can be null if no rendering is needed.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Handles the logic for when the heart collides with another game object.
     * If the heart collides with the paddle, a flag is set to add a new heart to the health UI.
     *
     * @param other     The game object that collided with the heart.
     * @param collision Information about the collision, including the collision normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        HealthUI.needToAddHeart = true;
        collisionHeartDelete = this;
    }

    /**
     * Gets the reference to the last heart that collided with the paddle.
     *
     * @return The heart instance that should be deleted after collision.
     */
    public static Heart getCollisionHeartDelete(){
        return collisionHeartDelete;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("paddle");
    }

    /**
     * Resets the reference to null.
     */
    public static void setCollisionHeart(){collisionHeartDelete = null;}
}
