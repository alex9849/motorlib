package net.alex9849.motorlib;

/**
 * This class is a Java port of https://github.com/waspinator/AccelStepper
 *
 */
public class AcceleratingStepper implements IStepperMotor {
    private final IStepperMotor stepperMotor;
    private long position;
    private long targetPosition;
    private double speed;
    private double maxSpeed;
    private double acceleration;
    private long n;
    private double c0;
    private double cn;
    private double cmin;
    private long stepInterval;
    private long lastStepTime;
    private final double doubleEpsilon = 0.00001d;


    public AcceleratingStepper(IStepperMotor stepper) {
        this.stepperMotor = stepper;
        this.position = 0;
        this.targetPosition = 0;
        this.speed = 0;
        this.maxSpeed = 1.0;
        this.acceleration = 0;
        this.lastStepTime = 0;
        this.n = 0;
        this.c0 = 0;
        this.cn = 0;
        this.cmin = 1;
        stepper.setDirection(Direction.FORWARD);
        this.setAcceleration(1);
    }

    public void setSpeed(double speed) {
        if(Math.abs(this.speed - speed) < doubleEpsilon) {
            return;
        }
        if(speed > maxSpeed || speed < -maxSpeed) {
            throw new IllegalArgumentException("Speed is larger as maxSpeed, or smaller as -maxSpeed");
        }
        if(Math.abs(speed) < doubleEpsilon) {
            stepInterval = 0;
        } else {
            stepInterval = (long) Math.abs(1000000.0 / speed);
            stepperMotor.setDirection((speed > 0) ? Direction.FORWARD : Direction.BACKWARD);
        }
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void moveTo(long absolute) {
        if(targetPosition != absolute) {
            targetPosition = absolute;
            computeNewSpeed();
        }
    }

    public void move(long relative) {
        moveTo(position + relative);
    }

    public long getTargetPosition() {
        return targetPosition;
    }

    public long getCurrentPosition() {
        return position;
    }

    public void setCurrentPosition(long position) {
        targetPosition = position;
        this.position = position;
        n = 0;
        stepInterval = 0;
        speed = 0;
    }

    public void setAcceleration(double acceleration) {
        if(acceleration < 0) {
            acceleration = -acceleration;
        }
        if(acceleration < doubleEpsilon) {
            return;
        }
        if (Math.abs(this.acceleration - acceleration) >= doubleEpsilon) {
            n = (long) (n * (this.acceleration / acceleration));
            c0 = 0.676 * Math.sqrt(2.0 / acceleration) * 1000000.0;
            this.acceleration = acceleration;
            computeNewSpeed();
        }
    }

    public long distanceToGo() {
        return targetPosition - position;
    }

    private void computeNewSpeed() {
        long distanceToGo = distanceToGo();
        long stepsToStop = (long) ((speed * speed) / (2 * acceleration));

        if(distanceToGo == 0 && stepsToStop <= 1) {
            stepInterval = 0;
            speed = 0;
            n = 0;
            return;
        }

        if(distanceToGo > 0) {
            if(n > 0) {
                if((stepsToStop >= distanceToGo) || stepperMotor.getDirection() == Direction.BACKWARD) {
                    n = -stepsToStop;
                }
            } else if(n < 0) {
                if((stepsToStop < distanceToGo) && stepperMotor.getDirection() == Direction.FORWARD) {
                    n = -n;
                }
            }
        } else  if(distanceToGo < 0) {
            if(n > 0) {
                if((stepsToStop >= -distanceToGo) || stepperMotor.getDirection() == Direction.FORWARD) {
                    n = -stepsToStop;
                }
            } else if(n < 0) {
                if((stepsToStop < -distanceToGo) && stepperMotor.getDirection() == Direction.BACKWARD) {
                    n = -n;
                }
            }
        }

        if(n == 0) {
            cn = c0;
            stepperMotor.setDirection((distanceToGo > 0) ? Direction.FORWARD : Direction.BACKWARD);
        } else {
            cn = cn - ((2.0 * cn) / ((4.0 * n) + 1));
            cn = Math.max(cn, cmin);
        }
        n++;
        stepInterval = (long) cn;
        speed = 1000000.0 / cn;
        if(stepperMotor.getDirection() == Direction.BACKWARD) {
            speed = -speed;
        }
    }

    public boolean runSpeed() {
        if(stepInterval == 0) {
            return false;
        }
        long time = System.nanoTime() / 1000;
        if(time - lastStepTime >= stepInterval) {
            if(stepperMotor.getDirection() == Direction.FORWARD) {
                position += 1;
            } else {
                position -= 1;
            }
            oneStep();
            lastStepTime = time;
            return true;
        } else {
            return false;
        }

    }

    public boolean run() {
        boolean madeStep = runSpeed();
        if(madeStep) {
            computeNewSpeed();
        }
        return madeStep;
    }

    public void runToPosition() {
        while (run()) {
            Thread.yield();
        }
    }

    public boolean runSpeedToPosition() {
        if (targetPosition == position)
            return false;
        if (targetPosition > position)
            stepperMotor.setDirection(Direction.FORWARD);
        else
            stepperMotor.setDirection(Direction.BACKWARD);
        return runSpeed();
    }

    public void runToNewPosition(long position) {
        moveTo(position);
        runToPosition();
    }

    public void setMaxSpeed(double speed) {
        if(speed < 0) {
            speed = -speed;
        }
        if(Math.abs(maxSpeed - speed) > doubleEpsilon) {
            this.maxSpeed = speed;
            this.cmin = 1000000.0 / speed;
            if(this.n > 0) {
                this.n = (long)((this.speed * this.speed) / (2.0 * this.acceleration));
                computeNewSpeed();
            }
        }
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public void stop() {
        if (speed != 0.0)
        {
            long stepsToStop = (long)((speed * speed) / (2.0 * acceleration)) + 1;
            if (speed > 0)
                move(stepsToStop);
            else
                move(-stepsToStop);
        }
    }

    public boolean isRunning() {
        return !(Math.abs(speed) < doubleEpsilon && targetPosition == position);
    }

    @Override
    public void setEnable(boolean enable) {
        stepperMotor.setEnable(enable);
    }

    @Override
    public boolean isEnabled() {
        return stepperMotor.isEnabled();
    }

    @Override
    public void oneStep() {
        stepperMotor.oneStep();
    }

    @Override
    public Direction getDirection() {
        return stepperMotor.getDirection();
    }

    @Override
    public void setDirection(Direction direction) {
        if(stepperMotor.getDirection() == direction) {
            return;
        }
        this.move(-1 * this.position - this.targetPosition);
    }
}
