package net.alex9849.pca9685port;

public class AcceleratingStepper implements StepperMotor {
    private final StepperMotor stepperMotor;
    private long position;
    private long targetPosition;
    private double speed; //steps per second
    private double targetSpeed; //target steps per second
    private double maxSpeed;
    private double acceleration;
    private long n;
    private double c0;
    private double cn;
    private double cmin;
    private long stepInterval;
    private long lastStepTime;
    private StepSize stepSize;
    private Direction direction;


    public AcceleratingStepper(StepperMotor stepper) {
        this.stepperMotor = stepper;
        this.position = 0;
        this.targetPosition = 0;
        this.speed = 0;
        this.targetSpeed = 0;
        this.maxSpeed = 1.0;
        this.acceleration = 0;
        this.lastStepTime = 0;
        this.n = 0;
        this.c0 = 0;
        this.cn = 0;
        this.cmin = 1;
        this.direction = Direction.FORWARD;
        this.stepSize = StepSize.SINGLE;
        this.setAcceleration(1);
    }

    public void setSpeed(double speed) {
        if(this.speed == speed) {
            return;
        }
        if(speed < -maxSpeed) {
            speed = -maxSpeed;
        } else if(speed > maxSpeed) {
            speed = maxSpeed;
        }
        if(speed == 0) {
            stepInterval = 0;
        } else {
            stepInterval = (long) Math.abs(1000000.0 / speed);
            direction = (speed > 0) ? Direction.FORWARD : Direction.BACKWARD;
        }
        this.speed = speed;
    }

    double getSpeed() {
        return speed;
    }

    void moveTo(long absolute) {
        if(targetPosition != absolute) {
            targetPosition = absolute;
            computeNewSpeed();
        }
    }

    void move(long relative) {
        moveTo(position + relative);
    }

    long getTargetPosition() {
        return targetPosition;
    }

    long getCurrentPosition() {
        return position;
    }

    void setCurrentPosition(long position) {
        targetPosition = position;
        this.position = position;
        n = 0;
        stepInterval = 0;
        speed = 0;
    }

    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
    }

    public void setAcceleration(double acceleration) {
        if(acceleration == 0) {
            return;
        }
        if(acceleration < 0) {
            acceleration = -acceleration;
        }
        if (this.acceleration != acceleration) {
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
                if((stepsToStop >= distanceToGo) || direction == Direction.BACKWARD) {
                    n = -stepsToStop;
                }
            } else if(n < 0) {
                if((stepsToStop < distanceToGo) && direction == Direction.FORWARD) {
                    n = -n;
                }
            }
        } else  if(distanceToGo < 0) {
            if(n > 0) {
                if((stepsToStop >= -distanceToGo) || direction == Direction.FORWARD) {
                    n = -stepsToStop;
                }
            } else if(n < 0) {
                if((stepsToStop < -distanceToGo) && direction == Direction.BACKWARD) {
                    n = -n;
                }
            }
        }

        if(n == 0) {
            cn = c0;
            direction = (distanceToGo > 0) ? Direction.FORWARD : Direction.BACKWARD;
        } else {
            cn = cn - ((2.0 * cn) / ((4.0 * n) + 1));
            cn = Math.max(cn, cmin);
        }
        n++;
        stepInterval = (long) cn;
        speed = 1000000.0 / cn;
        if(direction == Direction.BACKWARD) {
            speed = -speed;
        }
    }

    public boolean runSpeed() {
        if(stepInterval == 0) {
            return false;
        }
        long time = System.currentTimeMillis();
        if(time - lastStepTime >= stepInterval) {
            if(direction == Direction.FORWARD) {
                position += 1;
            } else {
                position -= 1;
            }
            oneStep(direction, stepSize);
            lastStepTime = time;
            return true;
        } else {
            return false;
        }

    }

    public boolean run() {
        if(runSpeed()) {
            computeNewSpeed();
        }
        return speed != 0.0 || distanceToGo() != 0;
    }

    void runToPosition() throws InterruptedException {
        while (run()) {
            Thread.yield();
        }
    }

    boolean runSpeedToPosition() {
        if (targetPosition == position)
            return false;
        if (targetPosition > position)
            direction = Direction.FORWARD;
        else
            direction = Direction.BACKWARD;
        return runSpeed();
    }

    void runToNewPosition(long position) throws InterruptedException {
        moveTo(position);
        runToPosition();
    }

    void setMaxSpeed(double speed) {
        if(speed < 0) {
            speed = -speed;
        }
        if(maxSpeed != speed) {
            maxSpeed = speed;
            cmin = 1000000.0 / speed;
            if(n > 0) {
                n = (long)((speed * speed) / (2.0 * acceleration));
                computeNewSpeed();
            }
        }
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    void stop() {
        if (speed != 0.0)
        {
            long stepsToStop = (long)((speed * speed) / (2.0 * acceleration)) + 1;
            if (speed > 0)
                move(stepsToStop);
            else
                move(-stepsToStop);
        }
    }

    boolean isRunning() {
        return !(speed == 0.0 && targetPosition == position);
    }

    @Override
    public void release() {
        stepperMotor.release();
    }

    @Override
    public int oneStep(Direction direction, StepSize style) {
        return stepperMotor.oneStep(direction, style);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
