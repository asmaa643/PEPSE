package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.util.JumpInterface;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.*;
import java.util.List;
import java.util.Objects;


/**
 * The manager of the game, implements the functional interface for the avatar.
 */
public class PepseGameManager extends GameManager implements JumpInterface {

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @see ImageReader
     * @see SoundReader
     * @see UserInputListener
     * @see WindowController
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        setSky(windowController.getWindowDimensions());
        Terrain terrain = buildTerrain(windowController);
        editSky(windowController.getWindowDimensions());

        double energy = Constants.FULL_ENERGY;
        EnergyDisplay energyDisplay = new EnergyDisplay(Vector2.ZERO, Vector2.ONES.mult(Block.SIZE),
                new TextRenderable(Constants.FULL_ENERGY_PERCENT));
        this.gameObjects().addGameObject(energyDisplay);

        AnimationRenderable animationRenderable = createAvatarRenderable(imageReader);
        float heightAtAvatarPLace = terrain.groundHeightAt(windowController.getWindowDimensions().x() / 2);
        float avatarDim =
                (float) Math.floor(heightAtAvatarPLace / Block.SIZE) * Block.SIZE + Constants.AVATAR_HEIGHT;

        createAvatar(imageReader, inputListener, windowController,
                avatarDim, animationRenderable, energy, energyDisplay);

        addFlora(windowController, terrain);
    }

    private AnimationRenderable createAvatarRenderable(ImageReader imageReader) {
        Renderable[] renderables = new Renderable[4];
        for (int i = 0; i < 4; i++) {
            Renderable renderable =
                    imageReader.readImage(Constants.IDLE_PATH + Integer.toString(i) + Constants.PNG_PATH,
                            true);
            renderables[i] = renderable;
        }
        AnimationRenderable animationRenderable = new AnimationRenderable(renderables,
                Constants.TIME_BETWEEN_CLIPS);
        animationRenderable.setRepeat(true);
        return animationRenderable;
    }

    private void addFlora(WindowController windowController, Terrain terrain) {
        Flora flora = new Flora(windowController.getWindowDimensions(), terrain::groundHeightAt);
        List<GameObject> floras = flora.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (GameObject f : floras) {
            if (Objects.equals(f.getTag(), Constants.LEAF_TAG)) {
                gameObjects().addGameObject(f, Layer.DEFAULT - 1);
                continue;
            } else if (Objects.equals(f.getTag(), Constants.FRUIT_TAG)) {
                gameObjects().addGameObject(f, Layer.DEFAULT);
                continue;
            }
            gameObjects().addGameObject(f, Layer.STATIC_OBJECTS);
        }
    }

    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              WindowController windowController, float avatarDim,
                              AnimationRenderable animationRenderable, double energy,
                              EnergyDisplay energyDisplay) {

        Avatar avatar = new Avatar(new Vector2(windowController.getWindowDimensions().x() / 2,
                windowController.getWindowDimensions().y() - avatarDim),
                inputListener,
                imageReader, animationRenderable, energy, energyDisplay::setEnergy, this);
        avatar.setTag(Constants.AVATAR_TAG);
        this.gameObjects().addGameObject(avatar, Layer.DEFAULT);
    }

    private void editSky(Vector2 windowDimensions) {
        GameObject night = Night.create(windowDimensions, Constants.DAY_CYCLE);
        this.gameObjects().addGameObject(night, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowDimensions, Constants.DAY_CYCLE);
        this.gameObjects().addGameObject(sun, Layer.BACKGROUND);

        GameObject sunHalo = SunHalo.create(sun);
        this.gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    private Terrain buildTerrain(WindowController windowController) {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

    private void setSky(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /**
     * Calls task for each Flora object.
     */
    @Override
    public void runWhenJump() {
        for (GameObject gameObject : this.gameObjects()) {
            if (Objects.equals(gameObject.getTag(), Constants.FRUIT_TAG)
                    || Objects.equals(gameObject.getTag(), Constants.LEAF_TAG)
                    || Objects.equals(gameObject.getTag(), Constants.TREE_TRUNK_TAG)) {
                FloraObject object = (FloraObject) gameObject;
                object.task();
            }
        }
    }

    /**
     * The main method.
     * @param args of the program (doesn't use them)
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
