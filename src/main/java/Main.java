import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import net.alex9849.motorlib.AcceleratingStepper;
import net.alex9849.motorlib.adafruit.AdafruitMotorkit;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Context pi4J = Pi4J.newAutoContext();
        long millis = System.currentTimeMillis();
        System.out.println("Start " + 0);
        I2CProvider i2CProvider = pi4J.provider("linuxfs-i2c");
        System.out.println("i2CProvider ready " + (System.currentTimeMillis() - millis));

        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x61).build();
        System.out.println("i2CConfig ready " + (System.currentTimeMillis() - millis));
        I2C i2c = i2CProvider.create(i2CConfig);
        System.out.println("i2c ready " + (System.currentTimeMillis() - millis));
        AdafruitMotorkit motorkit = new AdafruitMotorkit(i2c);

        System.out.println("motorkit ready " + (System.currentTimeMillis() - millis));
        AcceleratingStepper acceleratingStepper = new AcceleratingStepper(motorkit.getStepper2());
        //motorkit.getStepper1().release();

        if(true) {
            //acceleratingStepper.setMaxSpeed(610);
            acceleratingStepper.setMaxSpeed(20);
            acceleratingStepper.setAcceleration(300);
            acceleratingStepper.move(650 * 55);
            while (acceleratingStepper.distanceToGo() != 0) {
                acceleratingStepper.run();
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
