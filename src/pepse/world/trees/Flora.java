package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Represents the tress and their objects: fruits and leafs.
 */
public class Flora {

    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final int SPACE_FROM_AVATAR = 60;
    private static final double TREE_PROBABILITY = 0.2;
    private static final double LEAF_COUNT = 3.5;
    private static final double LEAF_PROBABILITY = 0.6;
    private static final double FRUIT_PROBABILITY = 0.1;
    private final Vector2 windowsDimensions;
    private final Function<Integer, Float> callback;
    private final float avatarPlace;

    /**
     * Constructs a Flora object.
     *
     * @param windowsDimensions of the game.
     * @param callback          to get the height of the ground in each coordinate.
     */
    public Flora(Vector2 windowsDimensions, Function<Integer, Float> callback) {
        this.windowsDimensions = windowsDimensions;
        this.callback = callback;
        avatarPlace = windowsDimensions.x() / 2;
    }

    /**
     * Creates the trees and their leaf and fruits in the given range.
     *
     * @param minX of the range.
     * @param maxX of the range.
     * @return a list of the created objects.
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        ArrayList<GameObject> trees = new ArrayList<>();
        minX = closestMultiple(minX);
        maxX = closestMultiple(maxX);
        boolean addedTree = false;
        for (int i = minX; i < maxX; i += Block.SIZE) {
            if (Math.abs(i - avatarPlace) >= SPACE_FROM_AVATAR) {
                if (addedTree) {
                    addedTree = false;
                    continue;
                }
                addedTree = true;
                buildTree(i, trees);
            }
        }
        return trees;
    }

    private void buildTree(int i, ArrayList<GameObject> trees) {
        Random random = new Random();
        if (random.nextDouble() < TREE_PROBABILITY) {
            float y = (float) Math.floor(this.callback.apply(i) / Block.SIZE) * Block.SIZE;
            RectangleRenderable rectangleRenderable =
                    new RectangleRenderable(TRUNK_COLOR);
            TreeTrunk treeTrunk = new TreeTrunk(new Vector2(i, windowsDimensions.y() - (y - Block.SIZE)),
                    Vector2.ONES, rectangleRenderable);
            treeTrunk.setTag(Constants.TREE_TRUNK_TAG);
            trees.add(treeTrunk);
            addLeafsAndFruits(trees,
                    (int) (i - (Block.SIZE * LEAF_COUNT)),
                    (int) (i + Block.SIZE + (Block.SIZE * LEAF_COUNT)),
                    (int) ((treeTrunk.getTopLeftCorner().y()) - (Block.SIZE * LEAF_COUNT)),
                    (int) ((treeTrunk.getTopLeftCorner().y()) + Block.SIZE + (Block.SIZE * LEAF_COUNT))
            );
        }
    }

    private void addLeafsAndFruits(ArrayList<GameObject> trees, int firstLeafX,
                                   int lastLeafX, int firstLeafY, int lastLeafY) {
        Random random = new Random();
        for (int k = firstLeafX; k < lastLeafX; k += Block.SIZE) {
            for (int j = firstLeafY; j < lastLeafY; j += Block.SIZE) {
                double d = random.nextDouble();
                if (d < LEAF_PROBABILITY) {
                    RectangleRenderable rectangleRenderable1 =
                            new RectangleRenderable(LEAF_COLOR);
                    Leaf leaf = new Leaf(new Vector2(k, j), Vector2.ONES.mult(Block.SIZE),
                            rectangleRenderable1);
                    leaf.setTag(Constants.LEAF_TAG);
                    trees.add(leaf);
                }
                if (d < FRUIT_PROBABILITY) {
                    OvalRenderable circle = new OvalRenderable(Color.RED);
                    Fruit fruit = new Fruit(new Vector2(k, j), Vector2.ONES.mult(Block.SIZE),
                            circle);
                    fruit.setTag(Constants.FRUIT_TAG);
                    trees.add(fruit);
                }
            }
        }
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
