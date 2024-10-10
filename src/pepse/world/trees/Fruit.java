package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;
import java.util.Objects;

/**
 * Represents a fruit object on the tree, implements a transparent task when the avatar jumps.
 */
public class Fruit extends GameObject implements FloraObject{
    private static final String TRANSPARENT_TAG = "transparentFruit";
    private static final Color TRANSPARENT_COLOR = new Color(0f, 0f, 0f, 0f);
    private static final int RED = 0;
    private static final int YELLOW = 1;
    private static final int TRANSPARENT = 2;
    private int color;
    private static int colorNameAll;

    /**
     * Construct a leaf GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        color = RED;
        colorNameAll = RED;
    }

    /**
     * Should collide only with the avatar.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && Objects.equals(other.getTag(), Constants.AVATAR_TAG);
    }

    /**
     * To transparent when collide with the avatar
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.color == TRANSPARENT) return;
        transparent();
        new ScheduledTask(this, Constants.DAY_CYCLE, false, this::color);
    }

    private void transparent (){
        renderer().setRenderable(new OvalRenderable(TRANSPARENT_COLOR));
        color = TRANSPARENT;
        setTag(TRANSPARENT_TAG);
    }

    private void color(){
        color = colorNameAll;
        setTag(Constants.FRUIT_TAG);
        if (this.color == RED){
            renderer().setRenderable(new OvalRenderable(Color.RED));
        } else if (this.color == YELLOW) {
            renderer().setRenderable(new OvalRenderable(Color.YELLOW));
        }
    }

    /**
     * Transparent color when the avatar jumps, until the next cycle.
     */
    @Override
    public void task(){
        new ScheduledTask(this, 0, false, this::changeColor);
    }

    private void changeColor(){
        if (this.color == TRANSPARENT){
            return;
        }
        if(this.color == YELLOW){
            renderer().setRenderable(new OvalRenderable(Color.RED));
            color = RED;
            colorNameAll = RED;
            return;
        }
        renderer().setRenderable(new OvalRenderable(Color.YELLOW));
        color = YELLOW;
        colorNameAll = YELLOW;
    }
}
