package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.Random;

/**
 * Represents a leaf object on the tree, implements a transparent task when the avatar jumps.
 */
public class Leaf extends Block implements FloraObject{
    private static final float TRANSITION_LEFT = -1.5f * (float) Math.PI;
    private static final float TRANSITION_RIGHT = 1.5f * (float) Math.PI;
    private static final float MAIN_TRANSITION_TIME = 0.5f;
    private static final float TRANSITION_INIT_DIMENSION = 1f;
    private static final float TRANSITION_FINAL_DIMENSION = 0.9f;
    private static final float TRANSITION_INIT_ANGLE = (float) Math.PI;
    private static final float TRANSITION_FINAL_ANGLE = 30 * (float) Math.PI;
    private static final float ROTATE_TRANSITION_TIME = 2f;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        Random random = new Random();
        float timeToWait = random.nextFloat();
        new ScheduledTask(this, timeToWait, false, this::twoTransitions);
    }

    private void twoTransitions() {
        new Transition<>(this, renderer()::setRenderableAngle,
                TRANSITION_LEFT, TRANSITION_RIGHT,
                Transition.CUBIC_INTERPOLATOR_FLOAT, MAIN_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new Transition<>(this,
                (f) -> setDimensions(Vector2.ONES.mult(Block.SIZE).mult(f)),
                TRANSITION_INIT_DIMENSION,
                TRANSITION_FINAL_DIMENSION,
                Transition.CUBIC_INTERPOLATOR_FLOAT, MAIN_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }


    /**
     * Rotates when the avatar jumps, until the next cycle.
     */
    @Override
    public void task() {
        new ScheduledTask(this, 0, false, this::rotate);
    }

    private void rotate() {
        new Transition<>(this, renderer()::setRenderableAngle,
                TRANSITION_INIT_ANGLE, TRANSITION_FINAL_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT, ROTATE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, null);
    }


}
