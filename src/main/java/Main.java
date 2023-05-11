import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.io.i2c.I2CRegister;
import net.alex9849.pca9685port.AdafruitMotorkit;

public class Main {

    public static void main(String... args) {
        Context pi4J = Pi4J.newAutoContext();
        I2CProvider i2CProvider = pi4J.getI2CProvider();

        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x60).build();
        I2C i2c = i2CProvider.create(i2CConfig);
        AdafruitMotorkit motorkit = new AdafruitMotorkit(i2c);
    }

}
