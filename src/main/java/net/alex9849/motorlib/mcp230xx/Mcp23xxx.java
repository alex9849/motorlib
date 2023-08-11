package net.alex9849.motorlib.mcp230xx;

import com.pi4j.io.i2c.I2C;
import net.alex9849.motorlib.pin.IOutputPin;

import java.util.HashMap;
import java.util.Map;

public abstract class Mcp23xxx {
    protected final I2C device;
    private final Map<Byte, IOutputPin> pinMap;

    public Mcp23xxx(I2C device) {
        this.device = device;
        this.pinMap = new HashMap<>();
    }

    /**
     * @return The raw GPIO output register. Each bit represents the output value of
     * the associated pin (0 = low, 1 = high), assuming that pin has been
     * configured as an output previously.
     */
    public abstract short getGpio();

    public abstract void setGpio(short val);

    public IOutputPin getPin(byte pin) {
        if(pin < 0 || pin > 15) {
            throw new IllegalArgumentException("Pin number must be 0-15");
        }
        if(!pinMap.containsKey(pin)) {
            pinMap.put(pin, new Mcp23xxxPin(pin, this));
        }
        return pinMap.get(pin);
    }

}
