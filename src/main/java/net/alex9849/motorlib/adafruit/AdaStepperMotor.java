package net.alex9849.motorlib.adafruit;

import net.alex9849.motorlib.IStepperMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdaStepperMotor implements IStepperMotor {
    private final List<PWMChannel> coils;
    private final List<Integer> curve;
    private final int microsteps;
    private int currentMicrostep;
    private IStepperMotor.Direction direction;
    private StepSize stepSize;

    public AdaStepperMotor(PWMChannel ain1, PWMChannel ain2,
                           PWMChannel bin1, PWMChannel bin2, int microsteps) {
        //Adafruit motorkit supports normal digital io pins. This port doesn't do that currently.
        this.coils = Arrays.asList(ain2, bin1, ain1, bin2);
        for(PWMChannel channel : this.coils) {
            if(channel.getFrequency() < 1500) {
                throw new IllegalArgumentException("PWMOut outputs for stepper coils must " +
                        "either be set to at least 1500 Hz or allow variable frequency.");
            }
        }
        if(microsteps < 2) {
            throw new IllegalArgumentException("Microsteps must be at least 2");
        }
        if(Math.floorMod(microsteps, 2) == 1) {
            throw new IllegalArgumentException("Microsteps must be even");
        }
        this.curve = new ArrayList<>();
        for(int i = 0; i <= microsteps + 1; i++) {
            this.curve.add((int) Math.round(0xFFFF * Math.sin(Math.PI / (2 * microsteps) * i)));
        }
        this.currentMicrostep = 0;
        this.microsteps = microsteps;
        this.stepSize = StepSize.SINGLE;
    }

    private void updateCoils(boolean microstepping) {
        int[] dutyCycles = new int[coils.size()];
        int trailingCoil = Math.floorMod(this.currentMicrostep / this.microsteps, coils.size());
        int leadingCoil = Math.floorMod(trailingCoil + 1, coils.size());
        int microstep = Math.floorMod(this.currentMicrostep, this.microsteps);
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

    public void enable(boolean value) {
        if(value) {
            this.updateCoils(false);
        } else {
            for(PWMChannel coil : this.coils) {
                coil.setDutyCycle(0);
            }
        }
    }

    @Override
    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public StepSize getStepSize() {
        return this.stepSize;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * @return The current microstep
     */
    public int oneStep(Direction direction) {
        int stepSize = 0;
        if(this.stepSize == StepSize.MICROSTEP) {
            stepSize = 1;
        } else {
            int halfstep = microsteps / 2;
            int fullstep = microsteps;
            int additionalMicrosteps = Math.floorMod(currentMicrostep, halfstep);
            if(additionalMicrosteps != 0) {
                if(direction == Direction.FORWARD) {
                    currentMicrostep += halfstep - additionalMicrosteps;
                } else {
                    currentMicrostep -= additionalMicrosteps;
                }
                stepSize = 0;
            } else if (this.stepSize == StepSize.INTERLEAVE) {
                stepSize = halfstep;
            }
            int currentInterleave = currentMicrostep / halfstep;
            if((this.stepSize == StepSize.SINGLE && Math.floorMod(currentInterleave, 2) == 1)
                    || (this.stepSize == StepSize.DOUBLE && Math.floorMod(currentInterleave, 2) == 0)) {
                stepSize = halfstep;
            } else if (this.stepSize == StepSize.SINGLE || this.stepSize == StepSize.DOUBLE) {
                stepSize = fullstep;
            }
        }

        if(direction == Direction.FORWARD) {
            currentMicrostep +=stepSize;
        } else {
            currentMicrostep -= stepSize;
        }
        updateCoils(this.stepSize == StepSize.MICROSTEP);
        return currentMicrostep;
    }

}
