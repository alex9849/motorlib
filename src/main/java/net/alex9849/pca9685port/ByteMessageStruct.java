package net.alex9849.pca9685port;

import com.pi4j.io.i2c.I2CRegister;

public class ByteMessageStruct {
    private final I2CRegister device;

    public ByteMessageStruct(I2CRegister device) {
        this.device = device;
    }

    public byte read() {
        //Todo This may have to be 1 or 2 bytes long
        //device.write(new byte[]{});
        //device.readByte();
        return device.readByte();
    }

    public void write(byte value) {
        //Todo This may need to by 2 bytes long
        device.write(value);
    }

}
