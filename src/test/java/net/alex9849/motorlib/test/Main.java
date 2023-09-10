package net.alex9849.motorlib.test;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.IOType;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.provider.Provider;
import net.alex9849.motorlib.mcp230xx.Mcp23017;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.PinState;

import java.util.Map;

public class Main {

    public static void main(String... args) throws InterruptedException {
        Context pi4J = Pi4J.newAutoContext();
        //Context pi4J = Pi4J.newContextBuilder().add(new MockPlatform()).build();
        long millis = System.currentTimeMillis();
        System.out.println("Start " + 0);
        I2CProvider i2CProvider = pi4J.provider("linuxfs-i2c");
        for(Map.Entry<IOType, Provider> entry : pi4J.getPlatform().providers().entrySet()) {
            System.out.println(entry.getValue().describe());
        }
        //I2CProvider i2CProvider = pi4J.getI2CProvider();
        System.out.println("i2CProvider ready " + (System.currentTimeMillis() - millis));

        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x27).build();
        System.out.println("i2CConfig ready " + (System.currentTimeMillis() - millis));
        I2C i2c = i2CProvider.create(i2CConfig);
        System.out.println("i2c ready " + (System.currentTimeMillis() - millis));

        Mcp23017 mcp23017 = new Mcp23017(i2c);
        IOutputPin pin = mcp23017.getOutputPin((byte) 8);

        for (int i = 0; i < 10; i++) {
            pin.digitalWrite(PinState.HIGH);
            Thread.sleep(1000);
            pin.digitalWrite(PinState.LOW);
            Thread.sleep(1000);
        }
    }

}
