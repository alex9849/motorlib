package net.alex9849.motorlib;

public interface IStepperMotor {

    void release();
    void enable();
    boolean isEnabled();
    default boolean run() {
        oneStep();
        return true;
    }
    void oneStep();
    Direction getDirection();
    void setDirection(Direction direction);


    enum Direction {
        FORWARD, BACKWARD;
    }

    enum MicroStepping {
        FULL, HALF, MICRO_4, MICRO_8, MICRO_16, MICRO_32
    }
}
