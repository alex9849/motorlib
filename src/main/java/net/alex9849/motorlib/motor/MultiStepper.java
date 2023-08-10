package net.alex9849.motorlib.motor;

import java.util.ArrayList;
import java.util.List;

public class MultiStepper {
    private List<AcceleratingStepper> stepperMotors;
    private boolean readyToRun;

    AcceleratingStepper pivotMotor;
    private long pivotStepsMade;
    private boolean pivotOvershooting;
    private double[] stepRate;
    private long[] stepsMade;

    public MultiStepper() {
        this.stepperMotors = new ArrayList<>();
        this.readyToRun = false;
    }

    public boolean addStepper(AcceleratingStepper motor) {
        if(readyToRun) {
            throw new IllegalArgumentException("MultiStepper already running!");
        }
        return stepperMotors.add(motor);
    }

    private void prepareRun() {
        long maxDistance = 0;

        for(AcceleratingStepper motor : stepperMotors) {
            long distance = Math.abs(motor.distanceToGo());
            if(distance > maxDistance) {
                maxDistance = distance;
                pivotMotor = motor;
            }
        }

        if(maxDistance == 0) {
            this.readyToRun = true;
            return;
        }
        this.stepperMotors.remove(pivotMotor);
        this.pivotStepsMade = 0;
        this.pivotOvershooting = false;

        long finalMaxDistance = maxDistance;
        this.stepRate = stepperMotors.stream().mapToDouble(x -> x.distanceToGo() / ((double) finalMaxDistance)).toArray();
        this.stepsMade = new long[stepperMotors.size()];
        this.readyToRun = true;
    }

    public boolean runRound() {
        if (!readyToRun) {
            prepareRun();
        }
        if(pivotMotor == null) {
            return false;
        }
        if(!pivotOvershooting && pivotMotor.run()) {
            pivotStepsMade++;
        }

        pivotOvershooting = false;
        boolean pivotRunning = pivotMotor.distanceToGo() != 0;
        boolean stillRunning = false;
        for(int i = 0; i < stepperMotors.size(); i++) {
            AcceleratingStepper currentMotor = stepperMotors.get(i);

            boolean tryStep = pivotStepsMade * stepRate[i] > stepsMade[i]
                    || Math.abs(pivotStepsMade * stepRate[i] - stepsMade[i]) < 0.0001;

            if(tryStep || (!pivotRunning && currentMotor.distanceToGo() != 0)) {
                if(currentMotor.run()) {
                    stepsMade[i]++;
                } else {
                    pivotOvershooting = true;
                }
            }
            stillRunning |= currentMotor.distanceToGo() != 0;
        }
        return stillRunning || pivotRunning;
    }
}
