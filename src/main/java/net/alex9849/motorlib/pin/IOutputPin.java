package net.alex9849.motorlib.pin;

public interface IOutputPin {

    void digitalWrite(PinState value);

    boolean isHigh();

    void digitalWriteAndWait(PinState state);

    void setWaitAfterWriteTimeNs(long waitAfterWriteTimeNs);

}
