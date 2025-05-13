package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that handles the collision of an object with the ball to activate turbo mode.
 * When a collision occurs, if turbo mode is not already active, it is enabled, increasing the ball's speed.
 */
 public class TurboCollisionStrategy extends BasicCollisionStrategy {

    /**
     * Constructs a new TurboCollision instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public TurboCollisionStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Defines the behavior when a game object collides with the ball.
     * If turbo mode is not already active, it activates turbo mode and increases the ball's speed.
     *
     * @param object1 The first object involved in the collision (the ball).
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        if(!BrickerGameManager.isTurbo) {
            brickerGameManager.setTurbo();
        }
    }
}
