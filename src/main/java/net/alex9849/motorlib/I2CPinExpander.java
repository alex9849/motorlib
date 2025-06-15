package net.alex9849.motorlib;

import net.alex9849.motorlib.pin.IOutputPin;

public interface I2CPinExpander {

    boolean isOpen();

    IOutputPin getOutputPin(byte pin);
}
