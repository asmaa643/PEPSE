package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

/**
 * Displays the energy og the avatar.
 */
public class EnergyDisplay extends GameObject {
    private static final String PERCENTAGE = "%";
    private final TextRenderable textRenderable;
    private double energy;


    /**
     * Construct display GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions    Width and height in window coordinates.
     * @param textRenderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public EnergyDisplay(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
        this.textRenderable = textRenderable;
        this.energy = Constants.FULL_ENERGY;
    }


    /**
     * Should be called once per frame.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.textRenderable.setString((int)energy + PERCENTAGE);
    }

    /**
     * Sets the energy to the given one.
     * @param energy to set for.
     */
    public void setEnergy(double energy){
        this.energy = energy;
    }
}
