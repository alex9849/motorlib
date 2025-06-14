package net.alex9849.motorlib.xl9535;

import com.pi4j.io.i2c.I2C;

public class XL9535 {

    enum PORT {
        OUTPUT_0(0x02b), INVERSION(0x04), CONFIG_0(0x06)

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
        this.buf[0] = (byte) 0x00;
        this.buf[1] = (byte) 0x00;

        this.i2c.writeRegister(PORT.INVERSION.register, this.buf);
        this.i2c.writeRegister(PORT.OUTPUT_0.register, this.buf);
        this.i2c.writeRegister(PORT.CONFIG_0.register, this.buf);
    }

    public boolean relay(int num, Boolean value) {
        assert num >= 0 && num <= 15;
        byte p = (byte) (num / 8);
        byte b = (byte) (1 << (num % 8));
        this.i2c.readRegister(PORT.OUTPUT_0.register, this.buf);
        if (value == null) {
            return (this.buf[p] & b) != 0;
        }
        this.buf[p] &= (byte) ~b;
        if (value) {
            this.buf[p] |= b;
        }
        this.i2c.writeRegister(PORT.CONFIG_0.register, this.buf);
        return value;
    }

}
