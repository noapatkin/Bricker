package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Represents a brick in the Bricker game.
 * A brick interacts with other game objects and triggers a collision strategy when hit.
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a new bricker.gameobjects.Brick instance.
     *
     * @param topLeftCorner     Position of the brick in window coordinates (pixels).
     *                          The top-left corner of the window is at (0,0).
     * @param dimensions        The width and height of the brick in window coordinates.
     * @param renderable        The renderable representation of the brick. Can be null if no rendering is
     *                          needed.
     * @param collisionStrategy The strategy to execute when the brick is involved in a collision.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Handles the logic for when the brick collides with another game object.
     * The collision strategy associated with the brick is executed upon collision.
     *
     * @param other     The game object that collided with the brick.
     * @param collision Information about the collision, including the collision normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}
