package net.alex9849.pca9685port;

public interface StepperMotor {

    void release();
    int oneStep(Direction direction, StepSize style);
    Direction getDirection();


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
