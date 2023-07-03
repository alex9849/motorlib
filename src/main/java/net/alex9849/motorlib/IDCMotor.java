package net.alex9849.motorlib;

public interface IDCMotor {

    void setRunning(boolean running);

    void setDirection(Direction direction);

    boolean isRunning();

    Direction getDirection();

}
