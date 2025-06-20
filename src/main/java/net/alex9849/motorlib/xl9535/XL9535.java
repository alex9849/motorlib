package net.alex9849.motorlib.xl9535;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.i2c.I2C;
import net.alex9849.motorlib.exception.I2cException;
import net.alex9849.motorlib.pin.I2CPinExpander;
import net.alex9849.motorlib.pin.IOutputPin;

public class XL9535 implements I2CPinExpander {

    enum PORT {
        OUTPUT_0(2), INVERSION_0(4), CONFIG_0(6);

        final int register;
        PORT(int register) {
            this.register = register;
        }
    }

    private final I2C i2c;
    private final byte[] buf = new byte[2];

    public XL9535(I2C i2c) {
        this(i2c, true);
    }

    public XL9535(I2C i2c, boolean reset) {
        this.i2c = i2c;
        if(reset) {
            this.reset();
        }
    }


    public synchronized void reset() {
        try {
            this.buf[0] = (byte) 0;
            this.buf[1] = (byte) 0;

            this.i2c.writeRegister(PORT.INVERSION_0.register, this.buf);
            this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
            this.i2c.writeRegister(PORT.CONFIG_0.register, this.buf);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    public synchronized IOutputPin getOutputPin(byte pin) {
        if (pin < 0 || pin > 15) {
            throw new IllegalArgumentException("Pin number must be 0-15");
        }
        return new XL9535Pin(pin, this);
    }

    public synchronized void writeAll(boolean value) {
        if (value) {
            writeAll((short) 65535);
        } else {
            writeAll((short) 0);
        }
    }

    public synchronized void writeAll(short value) {
        try {
            this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
            this.buf[0] = (byte) value;
            this.buf[1] = (byte) (value >> 8);
            this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    public short readAll() {
        try {
            this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
            return (short) ((this.buf[1] << 8) | this.buf[0]);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    public synchronized void writeRegister(int num, boolean value) {
        try {
            assert num >= 0 && num <= 15;
            byte p = (byte) (num / 8);
            byte b = (byte) (1 << (num % 8));
            this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
            this.buf[p] &= (byte) ~b;
            if (value) {
                this.buf[p] |= b;
            }
            this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    public synchronized boolean readRegister(int num) {
        try {
            assert num >= 0 && num <= 15;
            byte p = (byte) (num / 8);
            byte b = (byte) (1 << (num % 8));
            this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
            return (this.buf[p] & b) != 0;
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }

    }

    public synchronized boolean isOpen () {
        return i2c.isOpen();
    }

}
