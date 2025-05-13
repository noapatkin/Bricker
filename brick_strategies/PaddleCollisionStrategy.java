package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that extends the basic collision behavior.
 * In this strategy, when a collision occurs between two game objects, the first object is removed
 * and an extra paddle is created and added to the game if one does not already exist.
 */
 public class PaddleCollisionStrategy extends BasicCollisionStrategy {

    /**
     * Constructs a new PaddleCollision instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public PaddleCollisionStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Defines the behavior when two game objects collide.
     * Removes the first object from the game and creates an extra paddle if one doesn't already exist.
     *
     * @param object1 The first object involved in the collision.
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        if(brickerGameManager.hasExtraPaddle()) {
            ExtraPaddle extraPaddle = brickerGameManager.createExtraPaddle();
            brickerGameManager.addObject(extraPaddle);
        }
    }
}
