package net.alex9849.motorlib.pin;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.gpio.digital.DigitalOutput;
import net.alex9849.motorlib.exception.GpioPinException;

public class Pi4JOutputPin extends AbstractOutputPin {
    private DigitalOutput output;

    public Pi4JOutputPin(DigitalOutput digitalOutput) {
        this.output = digitalOutput;
    }



    @Override
    public void digitalWrite(PinState value) {
        try {
            if(value == PinState.HIGH) {
                this.output.high();
            } else {
                this.output.low();
            }
        } catch (Pi4JException e) {
            throw new GpioPinException(e);
        }
    }

    @Override
    public boolean isHigh() {
        try {
            return this.output.isHigh();
        } catch (Pi4JException e) {
            throw new GpioPinException(e);
        }
    }

}
