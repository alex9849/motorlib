package net.alex9849.motorlib.motor;

import net.alex9849.motorlib.pin.IPin;

public class DCMotor implements IDCMotor {
    private IPin runPin;
    private IPin dirPin;
    private IPin.PinState runningState;
    private Direction direction;
    private Boolean running;

    public DCMotor(IPin runPin, IPin dirPin, IPin.PinState runningState) {
        this.runPin = runPin;
        this.dirPin = dirPin;
        this.runningState = runningState;
        this.direction = null;
        this.running = null;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if(running) {
            this.runPin.digitalWrite(runningState);
        } else {
            if(runningState == IPin.PinState.HIGH) {
                this.runPin.digitalWrite(IPin.PinState.LOW);
            } else {
                this.runPin.digitalWrite(IPin.PinState.HIGH);
            }
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.direction == Direction.FORWARD) {
            this.dirPin.digitalWrite(IPin.PinState.HIGH);
        } else {
            this.dirPin.digitalWrite(IPin.PinState.LOW);
        }
    }

    @Override
    public boolean isRunning() {
        if(running == null) {
            running = runPin.isHigh() && runningState == IPin.PinState.HIGH;
        }
        return running;
    }

    @Override
    public Direction getDirection() {
        if(direction == null) {
            if(dirPin.isHigh()) {
                direction = Direction.FORWARD;
            } else {
                direction = Direction.BACKWARD;
            }
        }
        return direction;
    }
}
