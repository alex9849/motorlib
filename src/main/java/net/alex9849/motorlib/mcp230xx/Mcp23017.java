package net.alex9849.motorlib.mcp230xx;

import com.pi4j.io.i2c.I2C;
import net.alex9849.motorlib.pin.IPin;

public class Mcp23017 extends Mcp230xx {

    private final static byte MCP23017_ADDRESS = 0x20;
    private final static byte MCP23017_IODIRA = 0x00;
    private final static byte MCP23017_IODIRB = 0x01;
    private final static byte MCP23017_IPOLA = 0x02;
    private final static byte MCP23017_IPOLB = 0x03;
    private final static byte MCP23017_GPINTENA = 0x04;
    private final static byte MCP23017_DEFVALA = 0x06;
    private final static byte MCP23017_INTCONA = 0x08;
    private final static byte MCP23017_IOCON = 0x0A;
    private final static byte MCP23017_GPPUA = 0x0C;
    private final static byte MCP23017_GPPUB = 0x0D;
    private final static byte MCP23017_GPIOA = 0x12;
    private final static byte MCP23017_GPIOB = 0x13;
    private final static byte MCP23017_INTFA = 0x0E;
    private final static byte MCP23017_INTFB = 0x0F;
    private final static byte MCP23017_INTCAPA = 0x10;
    private final static byte MCP23017_INTCAPB = 0x11;

    public Mcp23017(I2C i2cDevice) {
        this(i2cDevice, true);
    }
    public Mcp23017(I2C i2cDevice, boolean reset) {
        super(i2cDevice);
        if(reset) {
            this.setIoDir((short) 0xFFFF);
            this.setGpio((short) 0x0000);
            this.ioCon = 0x4;
            this.write_u16le(MCP23017_IPOLA, (short) 0x0000);
        }
    }

    public void getGpio(short val) {
        this.write_u16le(MCP23017_GPIOA, val);
    }

    public void setGpio(short val) {
        this.write_u16le(MCP23017_GPIOA, val);
    }

    public short getGpioA() {
        return this.read_u8(MCP23017_GPIOA);
    }

    public void setGpioA(byte val) {
        this.write_u8(MCP23017_GPIOA, val);
    }

    public short getGpioB() {
        return this.read_u16le(MCP23017_GPIOB);
    }

    public void setGpioB(byte val) {
        this.write_u8(MCP23017_GPIOB, val);
    }

    public short getIoDir() {
        return this.read_u16le(MCP23017_IODIRA);
    }

    public void setIoDir(short value) {
        this.write_u16le(MCP23017_IODIRA, value);
    }

    public byte getIoDirA() {
        return this.read_u8(MCP23017_IODIRA);
    }

    public void setIoDirA(byte value) {
        this.write_u8(MCP23017_IODIRA, value);
    }

    public byte getIoDirB() {
        return this.read_u8(MCP23017_IODIRB);
    }

    public void setIoDirB(byte value) {
        this.write_u8(MCP23017_IODIRB, value);
    }

    public short getGppu() {
        return this.read_u16le(MCP23017_GPPUA);
    }

    public void setGppu(short value) {
        this.write_u16le(MCP23017_GPPUA, value);
    }

    public byte getGppuA() {
        return this.read_u8(MCP23017_GPPUA);
    }

    public void setGppuA(byte value) {
        this.write_u8(MCP23017_GPPUA, value);
    }

    public byte getGppuB() {
        return this.read_u8(MCP23017_GPPUB);
    }

    public void setGppuB(byte value) {
        this.write_u8(MCP23017_GPPUB, value);
    }

    public short getIpol() {
        return read_u16le(MCP23017_IPOLA);
    }

    public void setIpol(short val) {
        write_u16le(MCP23017_IPOLA, val);
    }

    public byte getIpolA() {
        return read_u8(MCP23017_IPOLA);
    }

    public void setIpolA(byte value) {
        write_u8(MCP23017_IPOLA, value);
    }

    public byte getIpolB() {
        return read_u8(MCP23017_IPOLB);
    }

    public void setIpolB(byte value) {
        write_u8(MCP23017_IPOLB, value);
    }

    public short getInterruptConfiguration() {
        return read_u16le(MCP23017_INTCONA);
    }

    public void setInterruptConfiguration(short value) {
        write_u16le(MCP23017_INTCONA, value);
    }

    public short getInterruptEnable() {
        return read_u16le(MCP23017_GPINTENA);
    }

    public void setInterruptEnable(short value) {
        write_u16le(MCP23017_GPINTENA, value);
    }

    public short getDefaultValue() {
        return read_u16le(MCP23017_DEFVALA);
    }

    public void setDefaultValue(short value) {
        write_u16le(MCP23017_DEFVALA, value);
    }

    public byte getIoControl() {
        return read_u8(MCP23017_IOCON);
    }

    public void setIoControl(byte value) {
        value &= ~0x80;
        write_u8(MCP23017_IOCON, value);
    }

    public short[] getFlag() {
        short intf = this.read_u16le(MCP23017_INTFA);
        short[] flags = new short[16];
        for(int i = 0; i < 16; i++) {
            flags[i] = (short) (intf & (1 << i));
        }
        return flags;
    }

    public short[] getFlagA() {
        short intfa = this.read_u8(MCP23017_INTFA);
        short[] flags = new short[8];
        for(int i = 0; i < 8; i++) {
            flags[i] = (short) (intfa & (1 << i));
        }
        return flags;
    }

    public short[] getFlagB() {
        short intfb = this.read_u8(MCP23017_INTFB);
        short[] flags = new short[8];
        for(int i = 0; i < 8; i++) {
            flags[i] = (short) (intfb & (1 << i));
        }
        return flags;
    }

    public short[] getIntCap() {
        short intCap = this.read_u16le(MCP23017_INTCAPA);
        short[] cap = new short[16];
        for(int i = 0; i < 16; i++) {
            cap[i] = (short) ((intCap >> i) & 1);
        }
        return cap;
    }

    public short[] getIntCapA() {
        short intCapA = this.read_u8(MCP23017_INTCAPA);
        short[] cap = new short[8];
        for(int i = 0; i < 8; i++) {
            cap[i] = (short) ((intCapA >> i) & 1);
        }
        return cap;
    }

    public short[] getIntCapB() {
        short intCapB = this.read_u8(MCP23017_INTCAPB);
        short[] cap = new short[8];
        for(int i = 0; i < 8; i++) {
            cap[i] = (short) ((intCapB >> i) & 1);
        }
        return cap;
    }

    public void cleanInts() {
        read_u16le(MCP23017_INTCAPA);
    }

    public void cleanIntsA() {
        read_u8(MCP23017_INTCAPA);
    }

    public void cleanIntsB() {
        read_u8(MCP23017_INTCAPB);
    }

    public IPin getPin(int pin) {
        if(pin < 0 || pin > 15) {
            throw new IllegalArgumentException("Pin number must be 0-15");
        }
        //Todo
    }

}
