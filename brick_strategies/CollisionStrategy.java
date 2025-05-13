package bricker.brick_strategies;

import danogl.GameObject;
/**
 * Interface representing a collision strategy for handling interactions between game objects.
 * Implementing classes must define the behavior that occurs when two objects collide.
 */
public interface CollisionStrategy {

    /**
     * Defines the behavior that occurs when two game objects collide.
     *
     * @param object1 The first object involved in the collision.
     * @param object2 The second object involved in the collision.
     */
    void onCollision(GameObject object1, GameObject object2);
}
