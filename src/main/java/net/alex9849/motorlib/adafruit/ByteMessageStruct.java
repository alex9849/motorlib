package net.alex9849.motorlib.adafruit;

import com.pi4j.io.i2c.I2CRegister;

public class ByteMessageStruct {
    private final I2CRegister device;

    public ByteMessageStruct(I2CRegister device) {
        this.device = device;
    }

    public byte read() {
        return device.readByte();
    }

    public void write(byte value) {
        device.write(value);
    }

}
