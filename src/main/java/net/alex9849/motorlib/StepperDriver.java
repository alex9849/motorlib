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
    public void setEnable(boolean enable) {
        if(enable) {
            enablePin.digitalWrite(LOW);
        } else {
            enablePin.digitalWrite(HIGH);
        }
        this.enabled = enable;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void oneStep() {
        if(!this.isEnabled()) {
            setEnable(true);
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
