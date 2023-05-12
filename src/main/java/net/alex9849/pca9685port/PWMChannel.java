package net.alex9849.pca9685port;

public class PWMChannel {
    private final PCA9685 pca;
    private final int index;

    public PWMChannel(PCA9685 pca, int index) {
        this.pca = pca;
        this.index = index;
    }

    public float getFrequency() {
        return pca.getFrequency();
    }

    public int getDutyCycle() {
        PWMRegisters.PWMSignal pwm = pca.pwm_regs.read(index);
        if(pwm.first == 0x1000) {
            return 0xFFFF;
        }
        return pwm.second << 4;
    }

    public void setDutyCycle(int value) {
        if(value < 0 || value > 0xFFFF) {
            throw new IllegalArgumentException("Out of range: value " + value + " not 0 <= value <= 65,535");
        }
        if(value == 0xFFFF) {
            pca.pwm_regs.write(index, new PWMRegisters.PWMSignal((short) 0x1000, (short) 0));
        } else {
            value = (value + 1) >> 4;
            pca.pwm_regs.write(index, new PWMRegisters.PWMSignal((short) 0, (short) value));
        }
    }

}
