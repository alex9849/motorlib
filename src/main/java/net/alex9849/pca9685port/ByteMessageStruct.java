package net.alex9849.pca9685port;

import com.pi4j.io.i2c.I2CRegister;

public class ByteMessageStruct {
    private final I2CRegister device;
    private final byte address;

    public ByteMessageStruct(I2CRegister device, byte register_address) {
        this.device = device;
        this.address = register_address;
    }

    public byte read() {
        byte[] send_buf = new byte[2];
        send_buf[0] = address;
        device.write(send_buf);
        device.readByte();
        return device.readByte();
    }

    public void write(byte value) {
        byte[] buf = new byte[2];
        buf[0] = this.address;
        buf[1] = value;
        device.write(buf);
    }

}
