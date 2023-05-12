package net.alex9849.pca9685port;

public class DCMotor {
    private final PWMChannel positivePwm;
    private final PWMChannel negativePwm;
    private Float throttle;
    private Decay decayMode;

    public DCMotor(PWMChannel positivePwm, PWMChannel negativePwm) {
        this.positivePwm = positivePwm;
        this.negativePwm = negativePwm;
        this.throttle = null;
        this.decayMode = Decay.FAST_DECAY;
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

    public enum Decay {
        FAST_DECAY, SLOW_DECAY
    }

}
