package net.alex9849.motorlib.mcp230xx;

import com.pi4j.io.i2c.I2C;

public class Mcp23xxx {
    protected final I2C device;

    public Mcp23xxx(I2C device) {
        this.device = device;
    }



}
