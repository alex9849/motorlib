import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import net.alex9849.pca9685port.AdafruitMotorkit;
import net.alex9849.pca9685port.DCMotor;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Context pi4J = Pi4J.newAutoContext();
        I2CProvider i2CProvider = pi4J.getI2CProvider();

        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x60).build();
        I2C i2c = i2CProvider.create(i2CConfig);
        AdafruitMotorkit motorkit = new AdafruitMotorkit(i2c);
        motorkit.getMotor1().setDecayMode(DCMotor.Decay.SLOW_DECAY);
        float f = motorkit.getFrequency();
        motorkit.getMotor1().setThrottle(1f);
        Thread.sleep(5000);
        motorkit.getMotor1().setThrottle(0f);
        Thread.sleep(5000);
    }

}
