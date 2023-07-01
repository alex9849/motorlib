package net.alex9849.motorlib;

import static net.alex9849.motorlib.IMotorDriverPin.PinState.HIGH;
import static net.alex9849.motorlib.IMotorDriverPin.PinState.LOW;

public class StepperDriver implements IStepperMotor {
    private final IMotorDriverPin enablePin;
    private final IMotorDriverPin stepPin;
    private final IMotorDriverPin directionPin;
    private Direction direction;
    private boolean enabled;

    public StepperDriver(IMotorDriverPin enablePin, IMotorDriverPin stepPin, IMotorDriverPin directionPin) {
        this.enablePin = enablePin;
        this.stepPin = stepPin;
        this.directionPin = directionPin;
        this.enabled = !enablePin.isHigh();
    }

    @Override
    public void release() {
        enablePin.digitalWrite(HIGH);
        this.enabled = false;
    }

    public void enable() {
        enablePin.digitalWrite(LOW);
        this.enabled = true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void oneStep() {
        if(!this.isEnabled()) {
            enable();
        }
        stepPin.digitalWrite(HIGH);
        stepPin.digitalWrite(LOW);
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
