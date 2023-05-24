package net.alex9849.motorlib.adafruit;

import com.pi4j.io.exception.IOIllegalValueException;
import com.pi4j.io.i2c.I2C;

public class PCA9685 implements AutoCloseable {
    private final int reference_clock_speed;
    protected final PCAChannels channels;

    private final ByteMessageStruct mode1_reg;
    private final ByteMessageStruct mode2_reg;
    private final ByteMessageStruct prescale_reg;
    protected final PWMRegisters pwm_regs;

    public PCA9685(I2C i2c_device, int reference_clock_speed) {
        this.reference_clock_speed = reference_clock_speed;
        mode1_reg = new ByteMessageStruct(i2c_device.getRegister(0x00));
        mode2_reg = new ByteMessageStruct(i2c_device.getRegister(0x01));
        prescale_reg = new ByteMessageStruct(i2c_device.getRegister(0xFE));
        pwm_regs = new PWMRegisters(i2c_device, (byte) 0x06);
        channels = new PCAChannels();
        this.reset();
    }

    public void reset() {
        this.mode1_reg.write((byte) 0x00);
    }

    public float getFrequency() {
        byte prescale_result = prescale_reg.read();
        if (prescale_result < 3) {
            throw new IOIllegalValueException();
        }
        return reference_clock_speed / 4096 / prescale_result;
    }

    public void setFrequency(float freq) {
        int prescale = (int) (reference_clock_speed / 4096 / freq + 0.5);
        if(prescale < 3) {
            throw new IllegalArgumentException("PCA9685 cannot output at the given frequency");
        }
        byte old_mode = this.mode1_reg.read();
        this.mode1_reg.write((byte) ((old_mode & 0x7F) | 0x10));
        this.prescale_reg.write((byte) prescale);
        this.mode1_reg.write(old_mode);
        try {
            Thread.sleep(5);
        } catch (InterruptedException ignored) {
        } finally {
            this.mode1_reg.write((byte) (old_mode | 0xA0));
        }
    }


    @Override
    public void close() throws Exception {
        deinit();
    }

    public void deinit() {
        reset();
    }


    class PCAChannels {
        private final PWMChannel[] channels;
        public PCAChannels() {
            this.channels = new PWMChannel[size()];
        }

        public int size() {
            return 16;
        }

        public PWMChannel get(int index) {
            if(index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException();
            }
            if(channels[index] == null) {
                channels[index] = new PWMChannel(PCA9685.this, index);
            }
            return channels[index];
        }
    }
}
