package net.alex9849.motorlib;

import com.pi4j.io.gpio.digital.DigitalOutput;

public class Pi4JMotorDriverPin implements IMotorPin {
    private DigitalOutput output;
    public Pi4JMotorDriverPin(DigitalOutput digitalOutput) {
        this.output = digitalOutput;
    }

    @Override
    public void digitalWrite(IMotorPin.PinState value) {
        if(value == IMotorPin.PinState.HIGH) {
            this.output.high();
        } else {
            this.output.low();
        }
    }

    @Override
    public boolean isHigh() {
        return this.output.isHigh();
    }

}
