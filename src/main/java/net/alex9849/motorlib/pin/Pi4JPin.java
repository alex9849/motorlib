package net.alex9849.motorlib.pin;

import com.pi4j.io.gpio.digital.DigitalOutput;

public class Pi4JPin implements IPin {
    private DigitalOutput output;
    public Pi4JPin(DigitalOutput digitalOutput) {
        this.output = digitalOutput;
    }

    @Override
    public void digitalWrite(IPin.PinState value) {
        if(value == IPin.PinState.HIGH) {
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
