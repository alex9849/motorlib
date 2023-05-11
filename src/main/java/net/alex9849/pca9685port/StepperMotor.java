package net.alex9849.pca9685port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepperMotor {
    private List<PWMChannel> coils;
    private List<Integer> curve;
    private int microsteps;
    private int currentMicrostep;

    public StepperMotor(PWMChannel ain1, PWMChannel ain2,
                        PWMChannel bin1, PWMChannel bin2, int microsteps) {
        //Adafruit motorkit supports normal digital io pins. This port doesn't do that currently.
        this.coils = Arrays.asList(ain1, ain2, bin1, bin2);
        this.currentMicrostep = 0;
        this.microsteps = microsteps;
        for(PWMChannel channel : this.coils) {
            if(channel.getFrequency() < 1500) {
                throw new IllegalArgumentException("PWMOut outputs for stepper coils must " +
                        "either be set to at least 1500 Hz or allow variable frequency.");
            }
        }
        if(microsteps < 2) {
            throw new IllegalArgumentException("Microsteps must be at least 2");
        }
        if(microsteps % 2 == 1) {
            throw new IllegalArgumentException("Microsteps must be even");
        }
        this.curve = new ArrayList<>();
        for(int i = 0; i <= microsteps; i++) {
            this.curve.add((int) Math.round(0xFFFF * Math.sin(Math.PI / (2 * microsteps) * i)));
        }
        this.updateCoils(false);
    }

    private void updateCoils(boolean microstepping) {
        int[] dutyCycles = new int[]{0, 0, 0, 0};
        int trailingCoil = (this.currentMicrostep / this.microsteps) % 4;
        int leadingCoil = (trailingCoil + 1) % 4;
        int microstep = this.currentMicrostep % this.microsteps;
        dutyCycles[leadingCoil] = this.curve.get(microstep);
        dutyCycles[trailingCoil] = this.curve.get(this.microsteps - microstep);

        if(!microstepping && (
                dutyCycles[leadingCoil] == dutyCycles[trailingCoil]
                        && dutyCycles[leadingCoil] > 0)){
            dutyCycles[leadingCoil] = 0xFFFF;
            dutyCycles[trailingCoil] = 0xFFFF;
        }

        for(int i = 0; i < coils.size(); i++) {
            coils.get(i).setDutyCycle(dutyCycles[i]);
        }
    }

    public void release() {
        for(PWMChannel coil : this.coils) {
            coil.setDutyCycle(0);
        }
    }

    /**
     * @return The current microstep
     */
    public int oneStep(Direction direction, StepSize style) {
        int stepSize = 0;
        if(style == StepSize.MICROSTEP) {
            stepSize = 1;
        } else {
            int halfstep = microsteps / 2;
            int fullstep = microsteps;
            int additionalMicrosteps = currentMicrostep % halfstep;
            if(additionalMicrosteps != 0) {
                if(direction == Direction.FORWARD) {
                    currentMicrostep += halfstep - additionalMicrosteps;
                } else {
                    currentMicrostep -= additionalMicrosteps;
                }
                stepSize = 0;
            } else if (style == StepSize.INTERLEAVE) {
                stepSize = halfstep;
            }
            int currentInterleave = currentMicrostep / halfstep;
            if((style == StepSize.SINGLE && currentInterleave % 2 == 1)
                    || (style == StepSize.DOUBLE && currentInterleave % 2 == 0)) {
                stepSize = halfstep;
            } else if (style == StepSize.SINGLE || style == StepSize.DOUBLE) {
                stepSize = fullstep;
            }
        }

        if(direction == Direction.FORWARD) {
            currentMicrostep +=stepSize;
        } else {
            currentMicrostep -= stepSize;
        }
        updateCoils(style == StepSize.MICROSTEP);
        return currentMicrostep;
    }



    public enum Direction {
        FORWARD(1), BACKWARD(2);

        final int v;
        Direction(int v) {
            this.v = v;
        }
    }

    public enum StepSize {
        SINGLE(1, new byte[]{0b0010, 0b0100, 0b0001, 0b1000}),
        DOUBLE(2, new byte[]{0b1010, 0b0110, 0b0101, 0b1001}),
        INTERLEAVE(3, new byte[]{0b1010, 0b0010, 0b0110, 0b0100, 0b0101, 0b0001, 0b1001, 0b1000}),
        MICROSTEP(4, new byte[]{});

        final int v;
        final byte[] bytes;
        StepSize(int v, byte[] bytes) {
            this.v = v;
            this.bytes = bytes;
        }
    }



}
