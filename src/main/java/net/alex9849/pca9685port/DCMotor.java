package net.alex9849.pca9685port;

import java.util.Optional;

public class DCMotor {
    private PWMChannel positivePwm;
    private PWMChannel negativePwm;
    private Optional<Float> throttle;
    private Decay decayMode;

    public DCMotor(PWMChannel positivePwm, PWMChannel negativePwm) {
        this.positivePwm = positivePwm;
        this.negativePwm = negativePwm;
        this.throttle = Optional.empty();
        this.decayMode = Decay.FAST_DECAY;
    }

    public Optional<Float> getThrottle() {
        return throttle;
    }

    public void setThrottle(Optional<Float> value) {
        if (value.isPresent() && (value.get() > 1.0 || value.get() < -1.0)) {
           throttle = value;
        }
        if(value.isEmpty()) {
            positivePwm.setDutyCycle(0);
            negativePwm.setDutyCycle(0);
        } else if(value.get() == 0) {
            positivePwm.setDutyCycle(0xFFFF);
            negativePwm.setDutyCycle(0xFFFF);
        } else {

            int dutyCycle = (int) (0xFFFF * Math.abs(value.get()));
            if(decayMode == Decay.SLOW_DECAY) {
                if(value.get() < 0) {
                    positivePwm.setDutyCycle(0xFFFF - dutyCycle);
                    negativePwm.setDutyCycle(0xFFFF);
                } else {
                    positivePwm.setDutyCycle(0xFFFF);
                    negativePwm.setDutyCycle(0xFFFF - dutyCycle);
                }
            } else {
                if(value.get() < 0) {
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
        FAST_DECAY, SLOW_DECAY;
    }

}
