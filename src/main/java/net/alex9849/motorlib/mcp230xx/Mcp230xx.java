package net.alex9849.motorlib.mcp230xx;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.i2c.I2C;
import net.alex9849.motorlib.exception.GpioPinException;
import net.alex9849.motorlib.exception.I2cException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Mcp230xx extends Mcp23xxx {
    public Mcp230xx(I2C i2cDevice) {
        super(i2cDevice);
    }

    protected synchronized short read_u16le(byte register) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            device.getRegister(register).read(buf, 2);
            buf.position(0);
            return buf.getShort();
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    protected synchronized void write_u16le(byte register, short value) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            buf.putShort(value);
            device.getRegister(register).write(buf);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    protected synchronized byte read_u8(int register) {
        try {
            return device.getRegister(register).readByte();
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }

    protected synchronized void write_u8(int register, byte value) {
        try {
            device.getRegister(register).write(value);
        } catch (Pi4JException e) {
            throw new I2cException(e);
        }
    }
}
