package net.alex9849.motorlib.pin;

public interface I2CPinExpander {

    boolean isOpen();

    IOutputPin getOutputPin(byte pin);
}
