package net.alex9849.motorlib;

import com.pi4j.io.gpio.digital.DigitalOutput;

import static net.alex9849.motorlib.IMotorPin.PinState.HIGH;
import static net.alex9849.motorlib.IMotorPin.PinState.LOW;

public class StepperDriver implements IStepperMotor {
    private final IMotorPin enablePin;
    private final IMotorPin stepPin;
    private final IMotorPin directionPin;
    private Direction direction;
    private boolean enabled;

    public StepperDriver(IMotorPin enablePin, IMotorPin stepPin, IMotorPin directionPin) {
        this.enablePin = enablePin;
        this.stepPin = stepPin;
        this.directionPin = directionPin;
        this.enabled = !enablePin.isHigh();
    }

    public StepperDriver(DigitalOutput enablePin, DigitalOutput stepPin, DigitalOutput directionPin) {
        this(new Pi4JMotorDriverPin(enablePin), new Pi4JMotorDriverPin(stepPin), new Pi4JMotorDriverPin(directionPin));
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
