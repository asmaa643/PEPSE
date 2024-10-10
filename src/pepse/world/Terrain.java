package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.util.NoiseGenerator;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * represents a terrain of the ground game
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final float THIRD = 3f;
    private static final int NOISE_FACTOR = 4;
    private final float groundHeightAtX0;
    private final Vector2 windowDimensions;
    private final NoiseGenerator noiseGenerator;

    /**
     * The terrain class constructor
     * @param windowDimensions of the game
     * @param seed of the game
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = windowDimensions.y() / THIRD;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, (int) this.groundHeightAtX0);
    }

    /**
     * This function creates the ground noise  of the game in x coordinate
     * @param x the coordinate to create the noise for.
     * @return the height created.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates the ground of the game at the given range.
     * @param minX of the range.
     * @param maxX of the range.
     * @return list of all the blocks of the ground.
     */
    public List<Block> createInRange(int minX, int maxX) {
        minX = closestMultiple(minX);
        maxX = closestMultiple(maxX);
        ArrayList<Block> arrayList = new ArrayList<>();
        for (int i = minX; i <= maxX; i+=Block.SIZE) {
            int height = (int) Math.floor(this.groundHeightAt(i)/Block.SIZE) * Block.SIZE;
            for (int j = 0; j < height; j+=Block.SIZE) {
                RectangleRenderable rectangleRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(i, windowDimensions.y()-j),
                        Vector2.ONES.mult(Block.SIZE),
                        rectangleRenderable);
                block.setTag(Constants.GROUND_TAG);
                arrayList.add(block);
            }
        }
        return arrayList;
    }

    private static int closestMultiple(int x) {
        int quotient = x / Block.SIZE;
        int lowerMultiple = Block.SIZE * quotient;
        int higherMultiple = Block.SIZE * (quotient + 1);
        if (Math.abs(x - lowerMultiple) <= Math.abs(x - higherMultiple)) {
            return lowerMultiple;
        } else {
            return higherMultiple;
        }
    }

}
