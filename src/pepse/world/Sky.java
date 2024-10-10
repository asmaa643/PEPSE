package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import java.awt.*;

/**
 * A class represents the sky.
 */
public class Sky {
    private static final String SKY_DECODE_COLOR = "#80C6E5";
    private static final Color BASIC_SKY_COLOR = Color.decode(SKY_DECODE_COLOR);

    /**
     * A method that creates the sky of the game.
     * @param windowDimensions of the game.
     * @return the sky created.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(Constants.SKY_TAG);
        return sky;
    }
}
