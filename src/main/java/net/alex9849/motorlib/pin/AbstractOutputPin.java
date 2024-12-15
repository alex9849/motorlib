package net.alex9849.motorlib.pin;

public abstract class AbstractOutputPin implements IOutputPin {
    private long waitAfterWriteTimeNs = 0;

    @Override
    public void digitalWriteAndWait(PinState state) {
        digitalWrite(state);
        activeWait(waitAfterWriteTimeNs);
    }

    @Override
    public void setWaitAfterWriteTimeNs(long waitAfterWriteTimeNs) {
        this.waitAfterWriteTimeNs = waitAfterWriteTimeNs;
    }

    protected void activeWait(long ns) {
        long start = System.nanoTime();
        while(true) {
            if (System.nanoTime() - start >= ns) {
                return;
            }
        }
    }
}
