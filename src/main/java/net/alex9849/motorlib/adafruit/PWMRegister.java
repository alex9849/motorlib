package net.alex9849.motorlib.adafruit;

import com.pi4j.io.i2c.I2CRegister;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PWMRegister {
    private final I2CRegister i2CRegister;
    private final int payload_size = 4;

    public PWMRegister(I2CRegister i2CRegister) {
        this.i2CRegister = i2CRegister;
    }

    public PWMSignal read() {
        ByteBuffer buf = ByteBuffer.allocate(payload_size)
                .order(ByteOrder.LITTLE_ENDIAN);
        i2CRegister.read(buf);
        buf.position(0);
        PWMSignal signal = new PWMSignal();
        signal.first = buf.getShort();
        signal.second = buf.getShort();
        return signal;
    }

    public void write(PWMSignal signal) {
        ByteBuffer buf = ByteBuffer.allocate(payload_size)
                .order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort(signal.first);
        buf.putShort(signal.second);
        i2CRegister.write(buf.array());
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
