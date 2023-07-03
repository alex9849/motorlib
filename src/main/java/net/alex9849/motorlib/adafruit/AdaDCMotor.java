package net.alex9849.motorlib.adafruit;

import net.alex9849.motorlib.Direction;
import net.alex9849.motorlib.IDCMotor;

public class AdaDCMotor implements IDCMotor {
    private final PWMChannel positivePwm;
    private final PWMChannel negativePwm;
    private Float throttle;
    private Decay decayMode;

    private Direction direction;

    public AdaDCMotor(PWMChannel positivePwm, PWMChannel negativePwm) {
        this.positivePwm = positivePwm;
        this.negativePwm = negativePwm;
        this.throttle = null;
        this.decayMode = Decay.FAST_DECAY;
        this.direction = Direction.FORWARD;
    }

    public Float getThrottle() {
        return throttle;
    }

    public void setThrottle(Float throttle) {
        if (throttle != null && (throttle > 1.0 || throttle < -1.0)) {
            throw new IllegalArgumentException("Value needs to be in [-1.0; 1.0]!");
        }
        this.throttle = throttle;
        if(throttle == null) {
            positivePwm.setDutyCycle(0);
            negativePwm.setDutyCycle(0);
        } else if(throttle == 0) {
            positivePwm.setDutyCycle(0xFFFF);
            negativePwm.setDutyCycle(0xFFFF);
        } else {

            int dutyCycle = (int) (0xFFFF * Math.abs(throttle));
            if(decayMode == Decay.SLOW_DECAY) {
                if(throttle < 0) {
                    positivePwm.setDutyCycle(0xFFFF - dutyCycle);
                    negativePwm.setDutyCycle(0xFFFF);
                } else {
                    positivePwm.setDutyCycle(0xFFFF);
                    negativePwm.setDutyCycle(0xFFFF - dutyCycle);
                }
            } else {
                if(throttle < 0) {
                    positivePwm.setDutyCycle(0);
                    negativePwm.setDutyCycle(dutyCycle);
                } else {
                    positivePwm.setDutyCycle(dutyCycle);
                    negativePwm.setDutyCycle(0);
                }
            }
        }
    }

    public Decay getDecayMode() {
        return decayMode;
    }

    public void setDecayMode(Decay decayMode) {
        this.decayMode = decayMode;
    }

    @Override
    public void setRunning(boolean running) {
        if(running) {
            if(this.direction == Direction.FORWARD) {
                setThrottle(1.0f);
            } else {
                setThrottle(-1.0f);
            }
        } else {
            this.setThrottle(0.0f);
        }
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.direction == Direction.FORWARD) {
            setThrottle(getThrottle());
        } else {
            setThrottle(-1 * getThrottle());
        }
    }

    @Override
    public boolean isRunning() {
        return getThrottle() != null && Math.abs(getThrottle()) > 0.0001;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    public enum Decay {
        FAST_DECAY, SLOW_DECAY
    }

}
