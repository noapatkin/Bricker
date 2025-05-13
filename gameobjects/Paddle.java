package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
/**
 * Represents the paddle in the Bricker game.
 * The paddle is controlled by user input and moves horizontally within the game window's bounds.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * Constructs a new bricker.gameobjects.Paddle instance.
     *
     * @param topLeftCorner    Position of the paddle in window coordinates (pixels).
     *                         The top-left corner of the window is at (0,0).
     * @param dimensions       The width and height of the paddle in window coordinates.
     * @param renderable       The renderable representation of the paddle. Can be null if no rendering
     *                         is needed.
     * @param inputListener    Listener for detecting user input (e.g., arrow keys for movement).
     * @param windowDimensions The dimensions of the game window, used to ensure the paddle stays within
     *                         bounds.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Updates the paddle's state. Responds to user input to move the paddle horizontally
     * and ensures it stays within the game window's borders.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        //making sure the paddle stay in windows' border
        stayInWindow();
    }

    /**
     * Ensures that the paddle remains within the horizontal bounds of the game window.
     * Adjusts the paddle's position if it exceeds the window's edges.
     */
    private void stayInWindow(){
        float dim = windowDimensions.x();
        if (this.getTopLeftCorner().x() < 0){
            this.setTopLeftCorner(new Vector2(0, getTopLeftCorner().y()));
        }
        if (this.getTopLeftCorner().x() > dim-getDimensions().x()){
            this.setTopLeftCorner(new Vector2(dim-getDimensions().x(), getTopLeftCorner().y()));
        }
    }
}
