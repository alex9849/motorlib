package net.alex9849.motorlib.mcp230xx;

import com.pi4j.io.i2c.I2C;

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
            this.setIoControl((byte) 0x4);
            this.write_u16le(MCP23017_IPOLA, (short) 0x0000);
        }
    }

    public short getGpio() {
        return this.read_u16le(MCP23017_GPIOA);
    }

    public void setGpio(short val) {
        this.write_u16le(MCP23017_GPIOA, val);
    }

    /**
     * @return The raw GPIO A output register. Each bit represents the
     * output value of the associated pin (0 = low, 1 = high), assuming that
     * pin has been configured as an output previously.
     */
    public short getGpioA() {
        return this.read_u8(MCP23017_GPIOA);
    }

    public void setGpioA(byte val) {
        this.write_u8(MCP23017_GPIOA, val);
    }

    /**
     * @return The raw GPIO B output register. Each bit represents the
     * output value of the associated pin (0 = low, 1 = high), assuming that
     * pin has been configured as an output previously.
     */
    public short getGpioB() {
        return this.read_u16le(MCP23017_GPIOB);
    }

    public void setGpioB(byte val) {
        this.write_u8(MCP23017_GPIOB, val);
    }

    /**
     * @return The raw IODIR direction register. Each bit represents
     * direction of a pin, either 1 for an input or 0 for an output mode.
     */
    public short getIoDir() {
        return this.read_u16le(MCP23017_IODIRA);
    }

    public void setIoDir(short value) {
        this.write_u16le(MCP23017_IODIRA, value);
    }

    /**
     * @return The raw IODIR A direction register. Each bit represents
     * direction of a pin, either 1 for an input or 0 for an output mode.
     */
    public byte getIoDirA() {
        return this.read_u8(MCP23017_IODIRA);
    }

    public void setIoDirA(byte value) {
        this.write_u8(MCP23017_IODIRA, value);
    }

    /**
     * @return The raw IODIR B direction register. Each bit represents
     * direction of a pin, either 1 for an input or 0 for an output mode.
     */
    public byte getIoDirB() {
        return this.read_u8(MCP23017_IODIRB);
    }

    public void setIoDirB(byte value) {
        this.write_u8(MCP23017_IODIRB, value);
    }

    /**
     * @return The raw GPPU pull-up register. Each bit represents
     * if a pull-up is enabled on the specified pin (1 = pull-up enabled,
     * 0 = pull-up disabled).  Note pull-down resistors are NOT supported!
     */
    public short getGppu() {
        return this.read_u16le(MCP23017_GPPUA);
    }

    public void setGppu(short value) {
        this.write_u16le(MCP23017_GPPUA, value);
    }

    /**
     * @return The raw GPPU A pull-up register. Each bit represents
     * if a pull-up is enabled on the specified pin (1 = pull-up enabled,
     * 0 = pull-up disabled). Note pull-down resistors are NOT supported!
     */
    public byte getGppuA() {
        return this.read_u8(MCP23017_GPPUA);
    }

    public void setGppuA(byte value) {
        this.write_u8(MCP23017_GPPUA, value);
    }

    /**
     * @return The raw GPPU B pull-up register. Each bit represents
     * if a pull-up is enabled on the specified pin (1 = pull-up enabled,
     * 0 = pull-up disabled).  Note pull-down resistors are NOT supported!
     */
    public byte getGppuB() {
        return this.read_u8(MCP23017_GPPUB);
    }

    public void setGppuB(byte value) {
        this.write_u8(MCP23017_GPPUB, value);
    }

    /**
     * @return The raw IPOL output register. Each bit represents the
     * polarity value of the associated pin (0 = normal, 1 = inverted), assuming that
     * pin has been configured as an input previously.
     */
    public short getIpol() {
        return read_u16le(MCP23017_IPOLA);
    }

    public void setIpol(short val) {
        write_u16le(MCP23017_IPOLA, val);
    }

    /**
     * @return The raw IPOL A output register. Each bit represents the
     * polarity value of the associated pin (0 = normal, 1 = inverted), assuming that
     * pin has been configured as an input previously.
     */
    public byte getIpolA() {
        return read_u8(MCP23017_IPOLA);
    }

    public void setIpolA(byte value) {
        write_u8(MCP23017_IPOLA, value);
    }

    /**
     * @return The raw IPOL B output register. Each bit represents the
     * polarity value of the associated pin (0 = normal, 1 = inverted), assuming that
     * pin has been configured as an input previously.
     */
    public byte getIpolB() {
        return read_u8(MCP23017_IPOLB);
    }

    public void setIpolB(byte value) {
        write_u8(MCP23017_IPOLB, value);
    }

    /**
     * @return The raw INTCON interrupt control register. The INTCON register
     * controls how the associated pin value is compared for the
     * interrupt-on-change feature. If a bit is set, the corresponding
     * I/O pin is  compared against the associated bit in the DEFVAL
     * register. If a bit value is clear, the corresponding I/O pin is
     * compared against the previous value.
     */
    public short getInterruptConfiguration() {
        return read_u16le(MCP23017_INTCONA);
    }

    public void setInterruptConfiguration(short value) {
        write_u16le(MCP23017_INTCONA, value);
    }

    /**
     * @return The raw GPINTEN interrupt control register. The GPINTEN register
     * controls the interrupt-on-change feature for each pin. If a bit is
     * set, the corresponding pin is enabled for interrupt-on-change.
     * The DEFVAL and INTCON registers must also be configured if any pins
     * are enabled for interrupt-on-change.
     */
    public short getInterruptEnable() {
        return read_u16le(MCP23017_GPINTENA);
    }

    public void setInterruptEnable(short value) {
        write_u16le(MCP23017_GPINTENA, value);
    }

    /**
     * @return The raw DEFVAL interrupt control register. The default comparison
     * value is configured in the DEFVAL register. If enabled (via GPINTEN
     * and INTCON) to compare against the DEFVAL register, an opposite value
     * on the associated pin will cause an interrupt to occur.
     */
    public short getDefaultValue() {
        return read_u16le(MCP23017_DEFVALA);
    }

    public void setDefaultValue(short value) {
        write_u16le(MCP23017_DEFVALA, value);
    }

    /**
     * @return The raw IOCON configuration register. Bit 1 controls interrupt
     * polarity (1 = active-high, 0 = active-low). Bit 2 is whether irq pin
     * is open drain (1 = open drain, 0 = push-pull). Bit 3 is unused.
     * Bit 4 is whether SDA slew rate is enabled (1 = yes). Bit 5 is if I2C
     * address pointer auto-increments (1 = no). Bit 6 is whether interrupt
     * pins are internally connected (1 = yes). Bit 7 is whether registers
     * are all in one bank (1 = no), this is silently ignored if set to ``1``.
     */
    public byte getIoControl() {
        return read_u8(MCP23017_IOCON);
    }

    public void setIoControl(byte value) {
        value &= ~0x80;
        write_u8(MCP23017_IOCON, value);
    }

    /**
     * @return Returns an array with the pin-numbers that caused an interrupt
     * port A: pins 0-7
     * port B: pins 8-15
     */
    public short[] getFlag() {
        short intf = this.read_u16le(MCP23017_INTFA);
        short[] flags = new short[16];
        for(int i = 0; i < 16; i++) {
            flags[i] = (short) (intf & (1 << i));
        }
        return flags;
    }

    /**
     * @return An array of pin-numbers that caused an interrupt in port A
     * pins: 0-7
     */
    public short[] getFlagA() {
        short intfa = this.read_u8(MCP23017_INTFA);
        short[] flags = new short[8];
        for(int i = 0; i < 8; i++) {
            flags[i] = (short) (intfa & (1 << i));
        }
        return flags;
    }

    /**
     * @return An array of pin-numbers that caused an interrupt in port B
     * pins: 8-15
     */
    public short[] getFlagB() {
        short intfb = this.read_u8(MCP23017_INTFB);
        short[] flags = new short[8];
        for(int i = 0; i < 8; i++) {
            flags[i] = (short) (intfb & (1 << i));
        }
        return flags;
    }

    /**
     * @return An array with the pin values at time of interrupt
     * port A: pins 0-7
     * port B: pins 8-15
     */
    public short[] getIntCap() {
        short intCap = this.read_u16le(MCP23017_INTCAPA);
        short[] cap = new short[16];
        for(int i = 0; i < 16; i++) {
            cap[i] = (short) ((intCap >> i) & 1);
        }
        return cap;
    }

    /**
     * @return An array of pin values at time of interrupt
     * pins: 0-7
     */
    public short[] getIntCapA() {
        short intCapA = this.read_u8(MCP23017_INTCAPA);
        short[] cap = new short[8];
        for(int i = 0; i < 8; i++) {
            cap[i] = (short) ((intCapA >> i) & 1);
        }
        return cap;
    }

    /**
     * @return An array of pin values at time of interrupt
     * pins: 8-15
     */
    public short[] getIntCapB() {
        short intCapB = this.read_u8(MCP23017_INTCAPB);
        short[] cap = new short[8];
        for(int i = 0; i < 8; i++) {
            cap[i] = (short) ((intCapB >> i) & 1);
        }
        return cap;
    }

    /**
     * Clears interrupts by reading INTCAP.
     */
    public void cleanInts() {
        read_u16le(MCP23017_INTCAPA);
    }

    /**
     * Clears port A interrupts.
     */
    public void cleanIntsA() {
        read_u8(MCP23017_INTCAPA);
    }

    /**
     * Clears port B interrupts.
     */
    public void cleanIntsB() {
        read_u8(MCP23017_INTCAPB);
    }

}
