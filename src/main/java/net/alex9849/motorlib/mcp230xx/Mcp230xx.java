package net.alex9849.motorlib.mcp230xx;

import com.pi4j.io.i2c.I2C;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Mcp230xx extends Mcp23xxx {
    public Mcp230xx(I2C i2cDevice) {
        super(i2cDevice);
    }

    protected short read_u16le(byte register) {
        ByteBuffer buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        device.getRegister(register).read(buf);
        return buf.getShort();
    }

    protected void write_u16le(byte register, short value) {
        ByteBuffer buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort(value);
        device.getRegister(register).write(buf);
    }

    protected byte read_u8(int register) {
        return device.getRegister(register).readByte();
    }

    protected void write_u8(int register, byte value) {
        device.getRegister(register).write(value);
    }
}
