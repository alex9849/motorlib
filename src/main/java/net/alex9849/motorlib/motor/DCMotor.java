package net.alex9849.motorlib.motor;

import net.alex9849.motorlib.pin.IOutputPin;

public class DCMotor implements IDCMotor {
    private IOutputPin runPin;
    private IOutputPin dirPin;
    private IOutputPin.PinState runningState;
    private Direction direction;
    private Boolean running;

    public DCMotor(IOutputPin runPin, IOutputPin dirPin, IOutputPin.PinState runningState) {
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
            if(runningState == IOutputPin.PinState.HIGH) {
                this.runPin.digitalWrite(IOutputPin.PinState.LOW);
            } else {
                this.runPin.digitalWrite(IOutputPin.PinState.HIGH);
            }
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.direction == Direction.FORWARD) {
            this.dirPin.digitalWrite(IOutputPin.PinState.HIGH);
        } else {
            this.dirPin.digitalWrite(IOutputPin.PinState.LOW);
        }
    }

    @Override
    public boolean isRunning() {
        if(running == null) {
            running = runPin.isHigh() && runningState == IOutputPin.PinState.HIGH;
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
