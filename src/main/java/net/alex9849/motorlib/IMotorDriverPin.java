package net.alex9849.motorlib;

public interface IMotorDriverPin {

    void digitalWrite(PinState value);

    enum PinState {
        HIGH, LOW
    }

}
