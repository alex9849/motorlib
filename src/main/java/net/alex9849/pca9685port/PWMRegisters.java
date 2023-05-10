package net.alex9849.pca9685port;

import com.pi4j.io.i2c.I2CRegister;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PWMRegisters {
    private final I2CRegister device;
    private final byte address;
    private final int payload_size = 4;
    private final int count;

    public PWMRegisters(I2CRegister device, byte register_address, int count) {
        //TODO Removed format and replaced it with mgs_size
        this.device = device;
        this.address = register_address;
        this.count = count;
    }

    private ByteBuffer getBuffer(int index) {
        if(0 > index || count <= index) {
            throw new IndexOutOfBoundsException();
        }
        return ByteBuffer.allocate(1 + payload_size)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put((byte) (address + payload_size * index));
    }

    public PWMSignal read(int index) {
        ByteBuffer buf = getBuffer(index);
        device.write(buf.array());
        for(int i = 0; i < payload_size; i++) {
            buf.put(device.readByte());
        }
        buf.position(1);
        PWMSignal signal = new PWMSignal();
        signal.first = buf.getShort();
        signal.second = buf.getShort();
        return signal;
    }

    public void write(int index, PWMSignal signal) {
        ByteBuffer buf = getBuffer(index);
        buf.putShort(signal.first)
                .putShort(signal.second);
        device.write(buf.array());
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
