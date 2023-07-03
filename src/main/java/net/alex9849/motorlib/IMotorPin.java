package net.alex9849.motorlib;

public interface IMotorPin {

    void digitalWrite(PinState value);

    boolean isHigh();

    enum PinState {
        HIGH, LOW
    }

}
