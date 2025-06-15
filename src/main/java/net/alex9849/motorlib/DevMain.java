package net.alex9849.motorlib;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.context.ContextBuilder;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalInputProvider;
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalOutputProvider;
import net.alex9849.motorlib.pin.Pi4JInputPin;
import net.alex9849.motorlib.pin.Pi4JOutputPin;
import net.alex9849.motorlib.sensor.HX711;


public class DevMain {

    public static void main(String... args) throws InterruptedException {
        ContextBuilder ctxBuilder = Pi4J.newContextBuilder();
        ctxBuilder.add(GpioDDigitalInputProvider.newInstance());
        ctxBuilder.add(GpioDDigitalOutputProvider.newInstance());
        Context pi4J = ctxBuilder.build();

        DigitalInputConfig configIn = DigitalInput
                .newConfigBuilder(pi4J)
                .address(5)
                .pull(PullResistance.OFF)
                .build();

        DigitalOutputConfig configOut = DigitalOutput
                .newConfigBuilder(pi4J)
                .address(6)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .build();

        DigitalInput dt = pi4J.digitalInput().create(configIn);
        DigitalOutput clk = pi4J.digitalOutput().create(configOut);

        HX711 hx711 = new HX711(new Pi4JInputPin(dt), new Pi4JOutputPin(clk), 32);

        long startTime = System.currentTimeMillis();
        long runTime = 1000 * 10;
        while (System.currentTimeMillis() < startTime + runTime) {
            System.out.println("Value: " + hx711.read_once());
        }

    }
}
