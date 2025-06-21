package net.alex9849.motorlib.pin;

import com.pi4j.io.i2c.I2C;

public interface I2CPinExpander {

    boolean isOpen();

    IOutputPin getOutputPin(byte pin);

    void updateI2c(I2C device, boolean reset);
}
