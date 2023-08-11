package net.alex9849.motorlib.mcp230xx;

import net.alex9849.motorlib.pin.IOutputPin;

public class Mcp23xxxPin implements IOutputPin {
    private final byte pinNr;
    private final Mcp23xxx mcp23xxx;

    public Mcp23xxxPin(byte pinNr, Mcp23xxx mcp23xxx) {
        this.pinNr = pinNr;
        this.mcp23xxx = mcp23xxx;
    }

    private boolean getBit(short val, byte bit) {
        return (val & (1 << bit)) > 0;
    }

    private short enableBit(short val, byte bit) {
        return (short) (val | (1 << bit));
    }

    private short clearBit(short val, byte bit) {
        return (short) (val & ~(1 << bit));
    }

    @Override
    public synchronized void digitalWrite(PinState value) {
        if(value == PinState.HIGH) {
            mcp23xxx.setGpio(enableBit(mcp23xxx.getGpio(), pinNr));
        } else {
            mcp23xxx.setGpio(clearBit(mcp23xxx.getGpio(), pinNr));
        }
    }

    @Override
    public synchronized boolean isHigh() {
        return getBit(mcp23xxx.getGpio(), pinNr);
    }


}
