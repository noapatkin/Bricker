package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * Implements a basic collision strategy for handling collisions between game objects.
 * In this strategy, when a collision occurs, one of the objects is removed from the game.
 */
 public class BasicCollisionStrategy implements CollisionStrategy {

    protected final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new BasicCollision instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Defines the behavior when two game objects collide.
     * Removes the first object from the game.
     *
     * @param object1 The first object involved in the collision.
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickerGameManager.removeObject(object1);
    }
}
