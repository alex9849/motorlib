package net.alex9849.motorlib;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import net.alex9849.motorlib.motor.AcceleratingStepper;
import net.alex9849.motorlib.motor.StepperDriver;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.Pi4JInputPin;
import net.alex9849.motorlib.pin.Pi4JOutputPin;
import net.alex9849.motorlib.pin.PinState;
import net.alex9849.motorlib.sensor.HX711;

import java.util.ArrayList;
import java.util.List;

public class DevMain {

    public static void main(String... args) throws InterruptedException {
        Context pi4J = Pi4J.newAutoContext();

        DigitalInput dt = pi4J.digitalInput().create(20);
        DigitalOutput clk = pi4J.digitalOutput().create(21);

        HX711 hx711 = new HX711(new Pi4JInputPin(dt), new Pi4JOutputPin(clk), 32);

        long startTime = System.currentTimeMillis();
        long runTime = 1000 * 10;
        while (System.currentTimeMillis() < startTime + runTime) {
            System.out.println("Value: " + hx711.read());
        }

    }
}
