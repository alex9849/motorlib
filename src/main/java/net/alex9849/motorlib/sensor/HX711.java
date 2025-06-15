package net.alex9849.motorlib.sensor;

import net.alex9849.motorlib.exception.HX711Exception;
import net.alex9849.motorlib.pin.IInputPin;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.PinState;

import java.util.Arrays;

public class HX711 {
    private final IOutputPin pinCLK;
    private final IInputPin pinDAT;
    private int gain;

    public long emptyValue = 0;
    public long calibrationValue = 0;
    public long calibrationWeight = 0;

    public HX711(IInputPin pinDAT, IOutputPin pinSCK, int gain) {
        this.pinCLK = pinSCK;
        this.pinDAT = pinDAT;
        setGain(gain);
        this.pinCLK.setWaitAfterWriteTimeNs(1_000_000); // 1 ms
        this.pinCLK.digitalWriteAndWait(PinState.HIGH);
        this.pinCLK.digitalWrite(PinState.LOW);
        this.pinCLK.setWaitAfterWriteTimeNs(1_000); //1 us
    }

    public synchronized long read() throws InterruptedException {
        return read(3);
    }

    public synchronized long read(int readRounds) throws InterruptedException {
        long[] reads = new long[readRounds];
        for (int i = 0; i < readRounds; i++) {
            reads[i] = read_once();
        }
        Arrays.sort(reads);
        if (reads.length % 2 == 0)
            return (long) (((double)reads[reads.length/2] + (double)reads[reads.length/2 - 1])/2);
        else
            return reads[reads.length/2];
    }

    public synchronized long read_once() throws InterruptedException {
        return read_once(1000);
    }

    public synchronized long read_once(long timeout) throws InterruptedException {
        pinCLK.digitalWrite(PinState.LOW);
        long waitStart = System.currentTimeMillis();
        while (!isReady()) {
            Thread.sleep(1);
            if(System.currentTimeMillis() - waitStart > timeout) {
                throw new HX711Exception("Reading load cell time out!");
            }
        }

        long readVal = 0;
        for (int i = 0; i < this.gain; i++) {
            pinCLK.digitalWriteAndWait(PinState.HIGH);
            readVal = readVal << 1;
            pinCLK.digitalWriteAndWait(PinState.LOW);
            if (pinDAT.isHigh()) {
                readVal++;
            }
        }

        pinCLK.digitalWriteAndWait(PinState.HIGH);
        readVal = readVal ^ 0x800000;
        pinCLK.digitalWriteAndWait(PinState.LOW);

        if(calibrationValue - emptyValue == 0) {
            return readVal - emptyValue;
        }

        double scaler = ((calibrationWeight)/((double)(calibrationValue - emptyValue)));
        double weight = ((readVal - emptyValue) * scaler);

        return (long) weight;
    }

    public synchronized void calibrateEmpty() throws InterruptedException {
        calibrateEmpty(10);
    }

    public synchronized void calibrateEmpty(int readRounds) throws InterruptedException {
        long calibrationValue = this.calibrationValue;
        long calibrationWeight = this.calibrationWeight;
        this.calibrationValue = 0;
        this.calibrationWeight = 0;
        this.emptyValue = 0;

        this.emptyValue = read(readRounds);

        this.calibrationValue = calibrationValue;
        this.calibrationWeight = calibrationWeight;

    }

    public synchronized void calibrateEmpty(long emptyValue) {
        this.emptyValue = emptyValue;
    }

    public synchronized void calibrateWeighted(long calibrationWeight) throws InterruptedException {
        calibrateWeighted(calibrationWeight, 10);
    }

    public synchronized void calibrateWeighted(long calibrationWeight, int readRounds) throws InterruptedException {
        long emptyValue = this.emptyValue;
        this.calibrationValue = 0;
        this.calibrationWeight = 0;
        this.emptyValue = 0;

        this.calibrationValue = read(readRounds);
        this.calibrationWeight = calibrationWeight;

        this.emptyValue = emptyValue;

    }

    public synchronized void calibrateWeighted(long calibrationWeight, long calibrationValue) {
        this.calibrationValue = calibrationValue;
        this.calibrationWeight = calibrationWeight;
    }

    public synchronized void setGain(int gain) {
        switch (gain) {
            case 128:       // channel A, gain factor 128
                this.gain = 24;
                break;
            case 64:        // channel A, gain factor 64
                this.gain = 26;
                break;
            case 32:        // channel B, gain factor 32
                this.gain = 25;
                break;
        }
    }

    public synchronized boolean isReady() {
        return !pinDAT.isHigh();
    }

}
