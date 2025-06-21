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

    private I2C device;
    private final byte[] buf = new byte[2];

    public XL9535(I2C device) {
        this(device, true);
    }

    public XL9535(I2C device, boolean reset) {
        this.device = device;
        if(reset) {
            this.reset();
        }
    }

    private synchronized void reset() {
        this.buf[0] = (byte) 0;
        this.buf[1] = (byte) 0;

        this.device.writeRegister(PORT.INVERSION_0.register, this.buf);
        this.device.writeRegister(PORT.OUTPUT_0.register, this.buf);
        this.device.writeRegister(PORT.CONFIG_0.register, this.buf);
    }

    public synchronized IOutputPin getOutputPin(byte pin) {
        if (pin < 0 || pin > 15) {
            throw new IllegalArgumentException("Pin number must be 0-15");
        }
        return new XL9535Pin(pin, this);
    }

    @Override
    public void updateI2c(I2C device, boolean reset) {
        this.device = device;
        if(reset) {
            this.reset();
        }
    }

    public synchronized void writeAll(boolean value) {
        if (value) {
            writeAll((short) 65535);
        } else {
            writeAll((short) 0);
        }
    }

    public synchronized void writeAll(short value) {
        this.device.readRegister(PORT.OUTPUT_0.register, this.buf);
        this.buf[0] = (byte) value;
        this.buf[1] = (byte) (value >> 8);
        this.device.writeRegister(PORT.OUTPUT_0.register, this.buf);
    }

    public short readAll() {
        this.device.readRegister(PORT.OUTPUT_0.register, this.buf);
        return (short) ((this.buf[1] << 8) | this.buf[0]);
    }

    public synchronized void writeRegister(int num, boolean value) {
        assert num >= 0 && num <= 15;
        byte p = (byte) (num / 8);
        byte b = (byte) (1 << (num % 8));
        this.device.readRegister(PORT.OUTPUT_0.register, this.buf);
        this.buf[p] &= (byte) ~b;
        if (value) {
            this.buf[p] |= b;
        }
        this.device.writeRegister(PORT.OUTPUT_0.register, this.buf);
    }

    public synchronized boolean readRegister(int num) {
        assert num >= 0 && num <= 15;
        byte p = (byte) (num / 8);
        byte b = (byte) (1 << (num % 8));
        this.device.readRegister(PORT.OUTPUT_0.register, this.buf);
        return (this.buf[p] & b) != 0;
    }

    public synchronized boolean isOpen () {
        return device.isOpen();
    }

}
