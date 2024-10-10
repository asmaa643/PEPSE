package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

/**
 * A class represents the sun.
 */
public class Sun {

    private static final int SUN_RADIUS = 100;
    private static final float START_CIRCLE = 0;
    private static final float END_CIRCLE = 360f;
    private static final float CYCLE_CENTER_X = 5f / 8;
    private static final float CYCLE_CENTER_Y = 2.5f;


    /**
     * A method that creates the sun in the sky.
     * @param windowDimensions of the game.
     * @param cycleLength of the day/night.
     * @return the sun created.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, Vector2.ONES.mult(SUN_RADIUS),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setCenter(new Vector2(windowDimensions.x() * (CYCLE_CENTER_X),
                windowDimensions.y() / CYCLE_CENTER_Y));
        sun.setTag(Constants.SUN_TAG);
        Vector2 initial = sun.getCenter();
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y());
        new Transition<Float>(sun,
                (Float angle) -> sun.setCenter(initial.subtract(cycleCenter).rotated(angle).add(cycleCenter))
                , START_CIRCLE, END_CIRCLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }

}
