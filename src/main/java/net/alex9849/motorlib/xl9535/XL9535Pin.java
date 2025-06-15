package net.alex9849.motorlib.xl9535;

import net.alex9849.motorlib.pin.AbstractOutputPin;
import net.alex9849.motorlib.pin.PinState;

public class XL9535Pin extends AbstractOutputPin {
    private final byte pinNr;
    private final XL9535 xl9535;

    public XL9535Pin(byte pinNr, XL9535 xl9535) {
        this.pinNr = pinNr;
        this.xl9535 = xl9535;
    }

    @Override
    public synchronized void digitalWrite(PinState value) {
        xl9535.writeRegister(pinNr, value == PinState.HIGH);
    }

    @Override
    public synchronized boolean isHigh() {
        return xl9535.readRegister(pinNr);
    }


}
