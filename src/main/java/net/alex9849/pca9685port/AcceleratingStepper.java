package net.alex9849.pca9685port;

public class AcceleratingStepper implements StepperMotor {
    private final StepperMotor stepperMotor;
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
    private StepSize stepSize;
    private Direction direction;
    private final double doubleEpsilon = 000001d;


    public AcceleratingStepper(StepperMotor stepper) {
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
        this.direction = Direction.FORWARD;
        this.stepSize = StepSize.SINGLE;
        this.setAcceleration(1);
    }

    public void setSpeed(double speed) {
        if(Math.abs(this.speed - speed) < doubleEpsilon) {
            return;
        }
        if(speed < -maxSpeed) {
            speed = -maxSpeed;
        } else if(speed > maxSpeed) {
            speed = maxSpeed;
        }
        if(Math.abs(speed) < doubleEpsilon) {
            stepInterval = 0;
        } else {
            stepInterval = (long) Math.abs(1000000.0 / speed);
            direction = (speed > 0) ? Direction.FORWARD : Direction.BACKWARD;
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

    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
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
            release();
            return false;
        }
        long time = System.nanoTime() / 1000;
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

    public void runToPosition() {
        while (run()) {
            Thread.yield();
        }
    }

    public boolean runSpeedToPosition() {
        if (targetPosition == position)
            return false;
        if (targetPosition > position)
            direction = Direction.FORWARD;
        else
            direction = Direction.BACKWARD;
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
