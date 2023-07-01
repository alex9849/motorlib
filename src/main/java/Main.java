import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import net.alex9849.motorlib.AcceleratingStepper;
import net.alex9849.motorlib.IMotorDriverPin;
import net.alex9849.motorlib.IStepperMotor;
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
        }

        StepperDriver stepperDriver = new StepperDriver(new Pin(enablePin1), new Pin(stepPin1), new Pin(dirPin1));
        AcceleratingStepper acceleratingStepper = new AcceleratingStepper(stepperDriver);
        enablePin1.high();
        //motorkit.getStepper1().release();

        long dir = 1;
        if(true) {
            while (true) {
                //acceleratingStepper.setMaxSpeed(610);
                acceleratingStepper.setMaxSpeed(8 * 2100);
                acceleratingStepper.setAcceleration(8 * 300);
                acceleratingStepper.move(8 * 650 * 30);
                acceleratingStepper.setDirection(acceleratingStepper.getDirection() == IStepperMotor.Direction.FORWARD? IStepperMotor.Direction.BACKWARD: IStepperMotor.Direction.FORWARD);
                while (acceleratingStepper.distanceToGo() != 0) {
                    acceleratingStepper.run();
                }
                dir = dir * -1;
            }
        } else {
            acceleratingStepper.setSpeed(-610);
            long endTime = System.currentTimeMillis() + 1000 * 60;
            while (endTime > System.currentTimeMillis()) {
                acceleratingStepper.runSpeed();
            }
        }
        acceleratingStepper.release();
        System.out.println("Finish");
        //System.out.println("Time to run: " + (System.currentTimeMillis() - timeToRun));
    }

}
