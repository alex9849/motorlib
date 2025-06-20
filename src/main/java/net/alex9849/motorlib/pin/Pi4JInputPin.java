package net.alex9849.motorlib.pin;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.gpio.analog.AnalogInput;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;
import net.alex9849.motorlib.exception.GpioPinException;

public class Pi4JInputPin implements IInputPin {
    private DigitalInput input;

    public Pi4JInputPin(DigitalInput input) {
        this.input = input;
    }

    @Override
    public boolean isHigh() {
        try {
            return input.isHigh();
        } catch (Pi4JException e) {
            throw new GpioPinException(e);
        }
    }

    @Override
    public boolean isPull() {
        try {
            return input.pull() == PullResistance.PULL_UP;
        } catch (Pi4JException e) {
            throw new GpioPinException(e);
        }
    }
}
