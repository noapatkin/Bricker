package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Represents a ball in the Bricker game.
 * The ball interacts with other game objects and reflects upon collision.
 */
 public class Ball extends GameObject {
    private int collisionCounter;
    private Sound collisionSound;

    /**
     * Constructs a new bricker.gameobjects.Ball instance.
     *
     * @param topLeftCorner  Position of the ball in window coordinates (pixels).
     *                       The top-left corner of the window is at (0,0).
     * @param dimensions     The width and height of the ball in window coordinates.
     * @param renderable     The renderable representation of the ball. Can be null if no rendering is
     *                       needed.
     * @param collisionSound The sound played when the ball collides with another object.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Handles the logic for when the ball collides with another game object.
     * The ball's velocity is updated to reflect off the collision surface.
     *
     * @param other     The game object that the ball collided with.
     * @param collision Information about the collision, including the collision normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionSound.play();
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        if(BrickerGameManager.isTurbo && this.getTag().equals("ball")) {
            collisionCounter++;
        }
    }

    /**
     * @return the amount of times the ball had collisions while in turbo mode
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

    /**
     * setting the amount of times for turbo, used mainly for restarting the counter
     * @param collusionCounter number of wanted times for the turbo counter
     */
    public void setCollusionCounter(int collusionCounter) {
        this.collisionCounter = collusionCounter;
    }
}
