package pepse.util;

/**
 * A functional interface to use when the avatar jumps.
 */
@FunctionalInterface
public interface JumpInterface {
    /**
     * Runs tasks when the avatar jumps.
     */
    public void runWhenJump();
}
