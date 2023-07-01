package net.alex9849.motorlib;

import static net.alex9849.motorlib.IMotorDriverPin.PinState.HIGH;
import static net.alex9849.motorlib.IMotorDriverPin.PinState.LOW;

public class StepperDriver implements IStepperMotor {
    private final IMotorDriverPin enablePin;
    private final IMotorDriverPin stepPin;
    private IMotorDriverPin.PinState prevStepPin;
    private final IMotorDriverPin directionPin;
    private Direction direction;
    private boolean initialized;

    public StepperDriver(IMotorDriverPin enablePin, IMotorDriverPin stepPin, IMotorDriverPin directionPin) {
        this.enablePin = enablePin;
        this.stepPin = stepPin;
        this.directionPin = directionPin;
        this.prevStepPin = LOW;
        this.initialized = false;
    }

    private void init() {
        setDirection(direction);
    }


    @Override
    public void release() {
        enablePin.digitalWrite(LOW);
    }

    @Override
    public void oneStep() {
        if(prevStepPin == LOW) {
            stepPin.digitalWrite(HIGH);
            prevStepPin = HIGH;
        } else {
            stepPin.digitalWrite(LOW);
            prevStepPin = LOW;
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.direction == Direction.FORWARD) {
            directionPin.digitalWrite(HIGH);
        } else {
            directionPin.digitalWrite(LOW);
        }
    }


    @Override
    public Direction getDirection() {
        return direction;
    }
}
