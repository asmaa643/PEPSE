package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.util.JumpInterface;

import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents the avatar object in the game
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 200;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 200;
    private static final float TIME_BETWEEN_CLIPS_JUMP = 0.6f;
    private static final double RUN_SPEND = 0.5;
    private static final double JUMP_SPEND = 10;
    private static final double IDLE_GET = 1;
    private static final double FRUIT_GET = 10;
    private static final int AVATAR_WIDTH = 30;
    private AnimationRenderable run;
    private AnimationRenderable jump;
    private AnimationRenderable stand;
    private Double energy;
    private final UserInputListener inputListener;
    private final Consumer<Double> callbackUpdate;
    private final JumpInterface callWhenJump;

    /**
     * Avatar class constructor
     *
     * @param pos                 initial position
     * @param inputListener       of the game
     * @param imageReader         of the game
     * @param animationRenderable of the game
     * @param energy              avatar energy
     * @param callbackUpdate      a consumer function to update the avatar energy
     * @param callWhenJump        FunctionalInterface to call run when jump
     */
    public Avatar(Vector2 pos,
                  UserInputListener inputListener,
                  ImageReader imageReader, AnimationRenderable animationRenderable, Double energy,
                  Consumer<Double> callbackUpdate, JumpInterface callWhenJump) {

        super(pos, new Vector2(AVATAR_WIDTH, Constants.AVATAR_HEIGHT), animationRenderable);
        this.callbackUpdate = callbackUpdate;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = energy;
        this.runAnimationRenderable(imageReader);
        this.jumpAnimationRenderable(imageReader);
        this.standAnimationRenderable(imageReader);
        this.callWhenJump = callWhenJump;
    }

    private void standAnimationRenderable(ImageReader imageReader) {
        Renderable[] standRenderable = new Renderable[4];
        for (int i = 0; i < 4; i++) {
            Renderable renderable =
                    imageReader.readImage(Constants.IDLE_PATH + Integer.toString(i) + Constants.PNG_PATH,
                            true);
            standRenderable[i] = renderable;
        }
        this.stand = new AnimationRenderable(standRenderable, Constants.TIME_BETWEEN_CLIPS);
    }

    private void jumpAnimationRenderable(ImageReader imageReader) {
        Renderable[] jumpRenderable = new Renderable[4];
        for (int i = 0; i < 4; i++) {
            Renderable renderable =
                    imageReader.readImage(Constants.JUMP_PATH + Integer.toString(i) + Constants.PNG_PATH,
                            true);
            jumpRenderable[i] = renderable;
        }
        this.jump = new AnimationRenderable(jumpRenderable, TIME_BETWEEN_CLIPS_JUMP);
    }

    private void runAnimationRenderable(ImageReader imageReader) {
        Renderable[] runRightLeftrenderable = new Renderable[6];
        for (int i = 0; i < 6; i++) {
            Renderable renderable =
                    imageReader.readImage(Constants.RUN_PATH + Integer.toString(i) + Constants.PNG_PATH,
                            true);
            runRightLeftrenderable[i] = renderable;
        }
        this.run = new AnimationRenderable(runRightLeftrenderable, Constants.TIME_BETWEEN_CLIPS);
    }

    /**
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
        float xVel = 0;
        xVel = getXVelWhenRun(xVel);
        transform().setVelocityX(xVel);
        jumpUpdate();
        idleUpdate();
    }

    private float getXVelWhenRun(float xVel) {
        if (energy >= RUN_SPEND && inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            energy -= RUN_SPEND;
            this.callbackUpdate.accept(energy);
            xVel -= VELOCITY_X;
            this.renderer().setRenderable(run);
            this.renderer().setIsFlippedHorizontally(true);
        }
        if (energy >= RUN_SPEND && inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            energy -= RUN_SPEND;
            this.callbackUpdate.accept(energy);
            xVel += VELOCITY_X;
            this.renderer().setRenderable(run);
            this.renderer().setIsFlippedHorizontally(false);
        }
        return xVel;
    }

    private void jumpUpdate() {
        if (energy >= JUMP_SPEND && inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            energy -= JUMP_SPEND;
            this.callbackUpdate.accept(energy);
            transform().setVelocityY(VELOCITY_Y);
            this.renderer().setRenderable(jump);

            this.callWhenJump.runWhenJump();
        } else if (energy < JUMP_SPEND && inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && getVelocity().y() == 0) {
            this.renderer().setRenderable(stand);
        }
    }

    private void idleUpdate() {
        if ((transform().getVelocity().x() == 0 && transform().getVelocity().y() == 0) || energy == 0) {
            energy += Math.min(Constants.FULL_ENERGY - energy, IDLE_GET);
            this.callbackUpdate.accept(energy);
            this.renderer().setRenderable(stand);
        }
    }


    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (Objects.equals(other.getTag(), Constants.FRUIT_TAG)) {
            energy += Math.min(Constants.FULL_ENERGY - energy, FRUIT_GET);
            this.callbackUpdate.accept(energy);
        }
    }
}



