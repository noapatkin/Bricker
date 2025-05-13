package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * A collision strategy for handling the collision of a heart object.
 * When a heart collides with another object, it is removed from the game and a new heart
 * is added to the game, which falls from the point of collision.
 */
 public class HeartCollisionStrategy extends BasicCollisionStrategy {
    private static final int FALL_SPEED = 100;
    private static final int SAFE_SPACE = 10;

    /**
            * Constructs a new HeartCollision instance.
            *
            * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public HeartCollisionStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Defines the behavior when a heart object collides with another game object.
     * Removes the heart from the game and adds a new heart that will fall from the collision point.
     *
     * @param object1 The first object involved in the collision (the heart).
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        if (brickerGameManager.removeObject(object1)) {
            brickerGameManager.addCollisionHeart(new Vector2(object1.getCenter().x(),
                    object1.getCenter().y() + SAFE_SPACE), FALL_SPEED);
        }
    }
}
