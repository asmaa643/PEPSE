package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.Random;

/**
 * Represents a tree trunk object on the tree, implements a transparent task when the avatar jumps.
 */
public class TreeTrunk extends Block implements FloraObject{
    private static final int SHORTEST_TREE = 6;
    private static final int LONGEST_TREE = 8;
    private static final Color TREE_TRUNK_COLOR = new Color(100, 50, 20);
    /**
     * Construct a tree trunk GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */

    public TreeTrunk(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner,dimensions, renderable);
        Random random = new Random();
        int height = random.nextInt(LONGEST_TREE - SHORTEST_TREE) + SHORTEST_TREE;
        setDimensions(new Vector2(Block.SIZE, Block.SIZE*height));
        setTopLeftCorner(new Vector2(topLeftCorner.x(), topLeftCorner.y() - Block.SIZE*height));
    }


    /**
     * Change to random color when the avatar jumps, until the next cycle.
     */
    @Override
    public void task(){
        new ScheduledTask(this, 0, false, this::newColor);
    }
    private void newColor(){
        RectangleRenderable rectangleRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TREE_TRUNK_COLOR));
        renderer().setRenderable(rectangleRenderable);
    }
}
