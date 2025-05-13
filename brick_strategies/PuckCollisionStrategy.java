package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that handles the collision of an object with the puck.
 * When a collision occurs, it creates two new puck objects at the collision point.
 */
 public class PuckCollisionStrategy extends BasicCollisionStrategy {

    /**
     * Constructs a new PuckCollision instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public PuckCollisionStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Defines the behavior when a game object collides with the puck.
     * It creates two new puck objects at the location of the collision.
     *
     * @param object1 The first object involved in the collision (the puck).
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        for(int i=0; i<2; i++){
            Ball puck = brickerGameManager.createPuck();
            puck.setCenter(object1.getCenter());
            brickerGameManager.addObject(puck);
        }
    }
}
