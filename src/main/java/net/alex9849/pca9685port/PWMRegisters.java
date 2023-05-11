package net.alex9849.pca9685port;

import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CRegister;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PWMRegisters {
    private final I2C i2C;
    private final I2CRegister[] device = new I2CRegister[16];
    private final byte address;
    private final int payload_size = 4;
    private final int count = 16;

    public PWMRegisters(I2C device, byte register_address) {
        //TODO Removed format and replaced it with mgs_size
        this.i2C = device;
        this.address = register_address;
    }

    private I2CRegister getRegister(int index) {
        if(0 > index || count <= index) {
            throw new IndexOutOfBoundsException();
        }
        if(device[index] == null) {
            device[index] = i2C.getRegister((byte) address + payload_size * index);
        }
        return device[index];
    }

    private ByteBuffer getBuffer(int index) {
        return ByteBuffer.allocate(payload_size)
                .order(ByteOrder.LITTLE_ENDIAN);
    }

    public PWMSignal read(int index) {
        ByteBuffer buf = ByteBuffer.allocate(payload_size)
                .order(ByteOrder.LITTLE_ENDIAN);
        I2CRegister register = getRegister(index);
        register.write(buf.array());
        register.read(buf);
        buf.position(0);
        PWMSignal signal = new PWMSignal();
        signal.first = buf.getShort();
        signal.second = buf.getShort();
        return signal;
    }

    public void write(int index, PWMSignal signal) {
        ByteBuffer buf = ByteBuffer.allocate(payload_size)
                .order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort(signal.first);
        buf.putShort(signal.second);
        getRegister(index).write(buf.array());
    }

    public int length() {
        return count;
    }

    public static class PWMSignal {
        public short first;
        public short second;

        public PWMSignal() {}

        public PWMSignal(short first, short second) {
            this.first = first;
            this.second = second;
        }
    }
}
