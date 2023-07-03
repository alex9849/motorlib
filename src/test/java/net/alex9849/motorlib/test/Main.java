package net.alex9849.motorlib.test;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import net.alex9849.motorlib.AcceleratingStepper;
import net.alex9849.motorlib.IMotorDriverPin;
import net.alex9849.motorlib.MultiStepper;
import net.alex9849.motorlib.StepperDriver;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Context pi4J = Pi4J.newAutoContext();
        /*
        long millis = System.currentTimeMillis();
        System.out.println("Start " + 0);
        I2CProvider i2CProvider = pi4J.provider("linuxfs-i2c");
        System.out.println("i2CProvider ready " + (System.currentTimeMillis() - millis));

        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x61).build();
        System.out.println("i2CConfig ready " + (System.currentTimeMillis() - millis));
        I2C i2c = i2CProvider.create(i2CConfig);
        System.out.println("i2c ready " + (System.currentTimeMillis() - millis));
        //AdafruitMotorkit motorkit = new AdafruitMotorkit(i2c);

        System.out.println("motorkit ready " + (System.currentTimeMillis() - millis));
         */

        DigitalOutputConfigBuilder cfgDirPin1 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(2)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput dirPin1 = pi4J.create(cfgDirPin1);

        DigitalOutputConfigBuilder cfgStepPin1 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(3)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput stepPin1 = pi4J.create(cfgStepPin1);

        DigitalOutputConfigBuilder cfgEnablePin1 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(4)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput enablePin1 = pi4J.create(cfgEnablePin1);

        DigitalOutputConfigBuilder cfgDirPin2 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(14)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput dirPin2 = pi4J.create(cfgDirPin2);

        DigitalOutputConfigBuilder cfgStepPin2 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(15)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput stepPin2 = pi4J.create(cfgStepPin2);

        DigitalOutputConfigBuilder cfgEnablePin2 = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(18)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output");

        DigitalOutput enablePin2 = pi4J.create(cfgEnablePin2);

        class Pin implements IMotorDriverPin {
            DigitalOutput output;

            Pin(DigitalOutput output) {
                this.output = output;
            }

            @Override
            public void digitalWrite(PinState value) {
                if(value == PinState.HIGH) {
                    this.output.high();
                } else {
                    this.output.low();
                }
            }

            @Override
            public boolean isHigh() {
                return this.output.isHigh();
            }
        }

        StepperDriver stepperDriver = new StepperDriver(new Pin(enablePin1), new Pin(stepPin1), new Pin(dirPin1));
        AcceleratingStepper acceleratingStepper = new AcceleratingStepper(stepperDriver);
        acceleratingStepper.setMaxSpeed(8 * 400);
        acceleratingStepper.setAcceleration(8 * 300);

        StepperDriver stepperDriver2 = new StepperDriver(new Pin(enablePin2), new Pin(stepPin2), new Pin(dirPin2));
        AcceleratingStepper acceleratingStepper2 = new AcceleratingStepper(stepperDriver2);
        acceleratingStepper2.setMaxSpeed(8 * 200);
        acceleratingStepper2.setAcceleration(8 * 300);

        long startTime = System.currentTimeMillis();

        acceleratingStepper.move(8 * 200 * 10);
        acceleratingStepper2.move(8 * 200 * 20);

        MultiStepper multiStepper = new MultiStepper();
        multiStepper.addStepper(acceleratingStepper);
        multiStepper.addStepper(acceleratingStepper2);
        multiStepper.prepareRun();
        while (multiStepper.runRound()) {
            Thread.yield();
        }

        /*int i = 0;
        while (i < 2) {
            acceleratingStepper.move(8 * 200 * 10);
            acceleratingStepper.setDirection(acceleratingStepper.getDirection() == IStepperMotor.Direction.FORWARD? IStepperMotor.Direction.BACKWARD: IStepperMotor.Direction.FORWARD);

            acceleratingStepper2.move(8 * 200 * 10);
            acceleratingStepper2.setDirection(acceleratingStepper2.getDirection() == IStepperMotor.Direction.FORWARD? IStepperMotor.Direction.BACKWARD: IStepperMotor.Direction.FORWARD);


            while (acceleratingStepper.distanceToGo() != 0 || acceleratingStepper2.distanceToGo() != 0) {
                acceleratingStepper.run();
                acceleratingStepper2.run();
            }
            i++;
        }*/
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime)+ "ms");
        System.out.println("Motor 1 finished: " + (acceleratingStepper.distanceToGo() == 0));
        System.out.println("Motor 2 finished: " + (acceleratingStepper2.distanceToGo() == 0));
        acceleratingStepper.release();
        acceleratingStepper2.release();
        System.out.println("Finish");
    }

}
