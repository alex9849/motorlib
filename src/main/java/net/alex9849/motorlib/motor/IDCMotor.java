package net.alex9849.motorlib.motor;

public interface IDCMotor extends IMotor {

    void setRunning(boolean running);

    void setDirection(Direction direction);

    boolean isRunning();

    Direction getDirection();

    @Override
    default void shutdown() {
        this.setRunning(false);
    }
}
