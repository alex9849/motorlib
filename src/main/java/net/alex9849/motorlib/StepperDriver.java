package net.alex9849.motorlib;

import com.pi4j.io.gpio.digital.DigitalOutput;
import net.alex9849.motorlib.motor.Direction;
import net.alex9849.motorlib.motor.IStepperMotor;
import net.alex9849.motorlib.pin.IPin;
import net.alex9849.motorlib.pin.Pi4JPin;

import static net.alex9849.motorlib.pin.IPin.PinState.HIGH;
import static net.alex9849.motorlib.pin.IPin.PinState.LOW;

public class StepperDriver implements IStepperMotor {
    private final IPin enablePin;
    private final IPin stepPin;
    private final IPin directionPin;
    private Direction direction;
    private Boolean enabled;

    public StepperDriver(IPin enablePin, IPin stepPin, IPin directionPin) {
        this.enablePin = enablePin;
        this.stepPin = stepPin;
        this.directionPin = directionPin;
    }

    public StepperDriver(DigitalOutput enablePin, DigitalOutput stepPin, DigitalOutput directionPin) {
        this(new Pi4JPin(enablePin), new Pi4JPin(stepPin), new Pi4JPin(directionPin));
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
        if(this.enabled == null) {
            this.enabled = !enablePin.isHigh();
        }
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
        if(direction == null) {
            if(directionPin.isHigh()) {
                direction = Direction.FORWARD;
            } else {
                direction = Direction.BACKWARD;
            }
        }
        return direction;
    }

    @Override
    public void shutdown() {
        IStepperMotor.super.shutdown();
        this.stepPin.digitalWrite(LOW);
        this.direction = null;
        this.enabled = null;
    }
}
