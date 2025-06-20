package net.alex9849.motorlib.pin;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.gpio.analog.AnalogInput;
import net.alex9849.motorlib.exception.GpioPinException;

public class Pi4JAnalogInput implements IAnalogInput {
    private final AnalogInput input;

    public Pi4JAnalogInput(AnalogInput input) {
        this.input = input;
    }

    @Override
    public int getValue() {
        try {
            return input.value();
        } catch (Pi4JException e) {
            throw new GpioPinException(e);
        }
    }
}
