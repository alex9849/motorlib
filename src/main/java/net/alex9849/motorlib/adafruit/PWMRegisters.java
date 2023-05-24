package net.alex9849.motorlib.adafruit;

import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CRegister;

public class PWMRegisters {
    private final int payload_size = 4;
    private final int count = 16;
    private final PWMRegister[] registers = new PWMRegister[16];

    public PWMRegisters(I2C device, byte register_address) {
        for(int i = 0; i < count; i++) {
            I2CRegister i2CRegister = device.getRegister( register_address + payload_size * i);
            registers[i] = new PWMRegister(i2CRegister);
        }
    }

    public PWMRegister get(int index) {
        if(0 > index || count <= index) {
            throw new IndexOutOfBoundsException();
        }
        return registers[index];
    }
}
