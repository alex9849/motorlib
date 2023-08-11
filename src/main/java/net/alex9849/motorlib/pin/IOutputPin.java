package net.alex9849.motorlib.pin;

public interface IOutputPin {

    void digitalWrite(PinState value);

    boolean isHigh();

    enum PinState {
        HIGH, LOW
    }

}
