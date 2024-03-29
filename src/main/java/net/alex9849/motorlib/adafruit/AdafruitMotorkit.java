package net.alex9849.motorlib.adafruit;

import com.pi4j.io.i2c.I2C;
import net.alex9849.motorlib.motor.IStepperMotor;

public class AdafruitMotorkit {
    private final IStepperMotor[] stepperMotors = new IStepperMotor[2];
    private final AdaDCMotor[] motors = new AdaDCMotor[4];

    private final PCA9685 pca;
    private final int steppersMicrosteps;

    public AdafruitMotorkit(I2C i2cDevice) {
        this(i2cDevice, 16, 1600);
    }

    public AdafruitMotorkit(I2C i2cDevice, int steppersMicrosteps, float pwmFrequency) {
        this.pca = new PCA9685(i2cDevice, 25000000);
        this.pca.setFrequency(pwmFrequency);
        this.steppersMicrosteps = steppersMicrosteps;
    }

    private AdaDCMotor getMotor(int idx, int ch1, int ch2, int ch3, int stepperIdx) {
        if(motors[idx] == null) {
            if(stepperMotors[stepperIdx] != null) {
                throw new RuntimeException("Cannot use motor" + (idx + 1) + " at the same time as stepper" + (stepperIdx + 1) + ".");
            }
            pca.channels.get(ch1).setDutyCycle(0xFFFF);
            motors[idx] = new AdaDCMotor(pca.channels.get(ch2), pca.channels.get(ch3));
        }
        return motors[idx];
    }

    public AdaDCMotor getMotor1() {
        return getMotor(0, 8, 9, 10, 0);
    }

    public AdaDCMotor getMotor2() {
        return getMotor(1, 13, 11, 12, 0);
    }

    public AdaDCMotor getMotor3() {
        return getMotor(2, 2, 3, 4, 1);
    }

    public AdaDCMotor getMotor4() {
        return getMotor(3, 7, 5, 6, 1);
    }

    public IStepperMotor getStepper1() {
        if(stepperMotors[0] == null) {
            if(motors[0] != null || motors[1] != null) {
                throw new RuntimeException("Cannot use stepper1 at the same time as motor1 or motor2.");
            }
            pca.channels.get(8).setDutyCycle(0xFFFF);
            pca.channels.get(13).setDutyCycle(0xFFFF);
            stepperMotors[0] = new AdaStepperMotor(
                    pca.channels.get(10),
                    pca.channels.get(9),
                    pca.channels.get(11),
                    pca.channels.get(12),
                    steppersMicrosteps
            );
        }
        return stepperMotors[0];
    }

    public IStepperMotor getStepper2() {
        if(stepperMotors[1] == null) {
            if(motors[2] != null || motors[3] != null) {
                throw new RuntimeException("Cannot use stepper2 at the same time as motor3 or motor4.");
            }
            pca.channels.get(7).setDutyCycle(0xFFFF);
            pca.channels.get(2).setDutyCycle(0xFFFF);
            stepperMotors[1] = new AdaStepperMotor(
                    pca.channels.get(4),
                    pca.channels.get(3),
                    pca.channels.get(5),
                    pca.channels.get(6),
                    steppersMicrosteps
            );
        }
        return stepperMotors[1];
    }

    public float getFrequency() {
        return pca.getFrequency();
    }

    public void setFrequency(float value) {
        pca.setFrequency(value);
    }

}
