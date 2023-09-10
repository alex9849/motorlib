package net.alex9849.motorlib.test;

import net.alex9849.motorlib.motor.AcceleratingStepper;
import net.alex9849.motorlib.motor.StepperDriver;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.PinState;

import java.util.ArrayList;
import java.util.List;

public class DevMain {

    public static class TimeData {
        long estimate;
        long currentTime;
        long distanceToGo;
    }


    public static void main(String... args) throws InterruptedException {

        IOutputPin dummyPin = new IOutputPin() {
            @Override
            public void digitalWrite(PinState value) {

            }

            @Override
            public boolean isHigh() {
                return true;
            }
        };

        StepperDriver driver = new StepperDriver(dummyPin, dummyPin, dummyPin);
        AcceleratingStepper acceleratingStepper = new AcceleratingStepper(driver);
        acceleratingStepper.setAcceleration(250);
        acceleratingStepper.setMaxSpeed(300);
        acceleratingStepper.move(1100);
        long timeStart = System.currentTimeMillis();
        boolean topSpeedReached = false;
        int stepNr = 0;
        List<TimeData> timeDataList = new ArrayList<>();
        while (acceleratingStepper.distanceToGo() != 0) {
            if(acceleratingStepper.run()) {
                if((stepNr % 1) == 0) {
                    TimeData td = new TimeData();
                    td.estimate = acceleratingStepper.estimateTimeTillCompletion();
                    td.currentTime = System.currentTimeMillis();
                    td.distanceToGo = acceleratingStepper.distanceToGo();
                    timeDataList.add(td);
                }
                stepNr++;
            }
            if(acceleratingStepper.getSpeed() == acceleratingStepper.getMaxSpeed() && !topSpeedReached) {
                System.out.println("TopSpeed after: " + (System.currentTimeMillis() - timeStart) + "ms");
                topSpeedReached = true;
            }
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - timeStart) + "ms");
        System.out.println("Estimates:");
        long currentTime = System.currentTimeMillis();
        for(TimeData td : timeDataList) {
            long actualTime = currentTime - td.currentTime;
            System.out.print(td.estimate + "/" + (actualTime) + " tdg(" + td.distanceToGo + ")");
            if(Math.abs(actualTime - td.estimate) > 25) {
                System.out.println(" WARN");
            } else {
                System.out.println();
            }
        }


    }
}
