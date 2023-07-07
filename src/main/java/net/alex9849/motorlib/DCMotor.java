package net.alex9849.motorlib;

public class DCMotor implements IDCMotor {
    private IMotorPin runPin;
    private IMotorPin dirPin;
    private IMotorPin.PinState runningState;
    private Direction direction;
    private Boolean running;

    public DCMotor(IMotorPin runPin, IMotorPin dirPin, IMotorPin.PinState runningState) {
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
            if(runningState == IMotorPin.PinState.HIGH) {
                this.runPin.digitalWrite(IMotorPin.PinState.LOW);
            } else {
                this.runPin.digitalWrite(IMotorPin.PinState.HIGH);
            }
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.direction == Direction.FORWARD) {
            this.dirPin.digitalWrite(IMotorPin.PinState.HIGH);
        } else {
            this.dirPin.digitalWrite(IMotorPin.PinState.LOW);
        }
    }

    @Override
    public boolean isRunning() {
        if(running == null) {
            running = runPin.isHigh() && runningState == IMotorPin.PinState.HIGH;
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
