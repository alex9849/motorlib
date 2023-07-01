package net.alex9849.motorlib;

public interface IMotorDriverPin {

    void digitalWrite(PinState value);

    boolean isHigh();

    enum PinState {
        HIGH, LOW
    }

}
