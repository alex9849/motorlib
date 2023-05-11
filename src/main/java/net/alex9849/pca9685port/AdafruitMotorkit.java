package net.alex9849.pca9685port;

import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CRegister;

public class AdafruitMotorkit {
    private final PCA9685 pca;
    private StepperMotor stepper1;
    private StepperMotor stepper2;
    private I2CRegister ic2;
    private final int steppersMicrosteps;

    public AdafruitMotorkit(byte address, I2C i2C) {
        this(address, i2C, 16, 1600);
    }

    public AdafruitMotorkit(byte address, I2C i2C, int steppersMicrosteps, float pwmFrequency) {
        this.ic2 = i2C.getRegister(address);
        this.pca = new PCA9685(ic2, 25000000);
        this.pca.setFrequency(pwmFrequency);
        this.steppersMicrosteps = steppersMicrosteps;
    }

    public StepperMotor getStepper1() {
        if(stepper1 == null) {
            pca.channels.get(8).setDutyCycle(0xFFFF);
            pca.channels.get(13).setDutyCycle(0xFFFF);
            stepper1 = new StepperMotor(
                    pca.channels.get(10),
                    pca.channels.get(9),
                    pca.channels.get(11),
                    pca.channels.get(12),
                    steppersMicrosteps
            );
        }
        return stepper1;
    }

    public StepperMotor getStepper2() {
        if(stepper2 == null) {
            pca.channels.get(7).setDutyCycle(0xFFFF);
            pca.channels.get(2).setDutyCycle(0xFFFF);
            stepper2 = new StepperMotor(
                    pca.channels.get(4),
                    pca.channels.get(3),
                    pca.channels.get(5),
                    pca.channels.get(6),
                    steppersMicrosteps
            );
        }
        return stepper2;
    }

    public float getFrequency() {
        return pca.getFrequency();
    }

    public void setFrequency(float value) {
        pca.setFrequency(value);
    }

}
