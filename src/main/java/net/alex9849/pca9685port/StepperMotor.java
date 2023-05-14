package net.alex9849.pca9685port;

public interface StepperMotor {

    void release();
    int oneStep(Direction direction, StepSize style);
    Direction getDirection();


    enum Direction {
        FORWARD, BACKWARD;
    }

    enum StepSize {
        SINGLE(new byte[]{0b0010, 0b0100, 0b0001, 0b1000}),
        DOUBLE(new byte[]{0b1010, 0b0110, 0b0101, 0b1001}),
        INTERLEAVE(new byte[]{0b1010, 0b0010, 0b0110, 0b0100, 0b0101, 0b0001, 0b1001, 0b1000}),
        MICROSTEP(new byte[]{});

        final byte[] bytes;
        StepSize(byte[] bytes) {
            this.bytes = bytes;
        }
    }
}
