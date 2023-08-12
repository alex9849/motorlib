package net.alex9849.motorlib.pin;

import com.pi4j.io.gpio.digital.DigitalOutput;

public class Pi4JOutputPin implements IOutputPin {
    private DigitalOutput output;
    public Pi4JOutputPin(DigitalOutput digitalOutput) {
        this.output = digitalOutput;
    }

    @Override
    public void digitalWrite(PinState value) {
        if(value == PinState.HIGH) {
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
