package net.alex9849.motorlib.motor;

import com.pi4j.io.gpio.digital.DigitalOutput;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.Pi4JOutputPin;

import java.util.Objects;

import static net.alex9849.motorlib.pin.PinState.HIGH;
import static net.alex9849.motorlib.pin.PinState.LOW;

public class StepperDriver implements IStepperMotor {
    private final IOutputPin enablePin;
    private final IOutputPin stepPin;
    private final IOutputPin directionPin;
    private Direction direction;
    private Boolean enabled;

    public StepperDriver(IOutputPin enablePin, IOutputPin stepPin, IOutputPin directionPin) {
        this.enablePin = enablePin;
        this.stepPin = stepPin;
        this.stepPin.setWaitAfterWriteTimeNs(2_500);
        this.directionPin = directionPin;
    }

    public StepperDriver(DigitalOutput enablePin, DigitalOutput stepPin, DigitalOutput directionPin) {
        this(new Pi4JOutputPin(enablePin), new Pi4JOutputPin(stepPin), new Pi4JOutputPin(directionPin));
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
        stepPin.digitalWriteAndWait(HIGH);
        stepPin.digitalWriteAndWait(LOW);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepperDriver that = (StepperDriver) o;
        return Objects.equals(enablePin, that.enablePin) && Objects.equals(stepPin, that.stepPin) && Objects.equals(directionPin, that.directionPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enablePin, stepPin, directionPin);
    }
}
