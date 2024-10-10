package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

/**
 * A class represents the sun halo.
 */
public class SunHalo {
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int SUN_HALO_RADIUS = 200;

    /**
     * A method that creates the sun in the sky.
     * @param sun that was created in the game.
     * @return the sun halo created.
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, Vector2.ONES.mult(SUN_HALO_RADIUS),
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(Constants.SUN_HALO_TAG);
        sunHalo.addComponent((float deltaTime) -> {
            sunHalo.setCenter(sun.getCenter());
        });
        return sunHalo;
    }

}
