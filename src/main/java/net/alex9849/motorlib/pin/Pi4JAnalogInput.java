package net.alex9849.motorlib.pin;

import com.pi4j.io.gpio.analog.AnalogInput;

public class Pi4JAnalogInput implements IAnalogInput {
    private final AnalogInput input;

    public Pi4JAnalogInput(AnalogInput input) {
        this.input = input;
    }

    @Override
    public int getValue() {
        return input.value();
    }
}
