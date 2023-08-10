package net.alex9849.motorlib.motor;

public interface IStepperMotor extends IMotor {

    /**
     * Sets the motor to enabled or not. If enabled the motor will get current applied by the motor driver.
     * @param enable True, if the motor should be enabled. False, if it should be disabled/released)
     */
    void setEnable(boolean enable);

    /**
     *
     * @return True, if the motor is enabled. False, if it is disabled/released)
     */
    boolean isEnabled();

    /**
     * Tried to perform one step
     * @return True, is the step has been performed. False, if not.
     */
    default boolean run() {
        oneStep();
        return true;
    }

    /**
     * Performs exactly one step
     */
    void oneStep();
    Direction getDirection();
    void setDirection(Direction direction);

    @Override
    default void shutdown() {
        this.setEnable(false);
    }
}
