package net.alex9849.motorlib.xl9535;

import com.pi4j.io.i2c.I2C;
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
    private byte[] buf = new byte[2];

    public XL9535(I2C i2c) {
        this.i2c = i2c;
        this.init();
    }

    private void init() {
        this.buf[0] = (byte) 0;
        this.buf[1] = (byte) 0;

        this.i2c.writeRegister(PORT.INVERSION_0.register, this.buf);
        this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
        this.i2c.writeRegister(PORT.CONFIG_0.register, this.buf);
    }

    public synchronized IOutputPin getOutputPin(byte pin) {
        if (pin < 0 || pin > 15) {
            throw new IllegalArgumentException("Pin number must be 0-15");
        }
        return new XL9535Pin(pin, this);
    }

    public void writeAll(boolean value) {
        if (value) {
            writeAll((short) 65535);
        } else {
            writeAll((short) 0);
        }
    }

    public void writeAll(short value) {
        this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
        this.buf[0] = (byte) value;
        this.buf[1] = (byte) (value >> 8);
        this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
    }

    public short readAll() {
        this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
        return (short) ((this.buf[1] << 8) | this.buf[0]);
    }

    public void writeRegister(int num, boolean value) {
        assert num >= 0 && num <= 15;
        byte p = (byte) (num / 8);
        byte b = (byte) (1 << (num % 8));
        this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
        this.buf[p] &= (byte) ~b;
        if (value) {
            this.buf[p] |= b;
        }
        this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
    }

    public boolean readRegister(int num) {
        assert num >= 0 && num <= 15;
        byte p = (byte) (num / 8);
        byte b = (byte) (1 << (num % 8));
        this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
        return (this.buf[p] & b) != 0;
    }

    public boolean isOpen () {
        return i2c.isOpen();
    }

}
