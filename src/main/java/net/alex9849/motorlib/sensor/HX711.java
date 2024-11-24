package net.alex9849.motorlib.sensor;

import net.alex9849.motorlib.pin.IInputPin;
import net.alex9849.motorlib.pin.IOutputPin;
import net.alex9849.motorlib.pin.PinState;

public class HX711 {
    private final IOutputPin pinCLK;
    private final IInputPin pinDAT;
    private int gain;

    public long emptyValue = 0;
    public long calibrationValue = 0;
    public long calibrationWeight = 0;

    public HX711(IInputPin pinDAT, IOutputPin pinSCK, int gain) throws InterruptedException {
        this.pinCLK = pinSCK;
        this.pinDAT = pinDAT;
        setGain(gain);
    }

    public long read() throws InterruptedException {
        pinCLK.digitalWrite(PinState.LOW);
        while (!isReady()) {
            Thread.sleep(1);
        }

        long readVal = 0;
        for (int i = 0; i < this.gain; i++) {
            pinCLK.digitalWrite(PinState.HIGH);
            readVal = readVal << 1;
            pinCLK.digitalWrite(PinState.LOW);
            if (pinDAT.isHigh()) {
                readVal++;
            }
        }

        pinCLK.digitalWrite(PinState.HIGH);
        readVal = readVal ^ 0x800000;
        pinCLK.digitalWrite(PinState.LOW);

        if(calibrationValue - emptyValue == 0) {
            return readVal - emptyValue;
        }

        double scaler = ((calibrationWeight)/((double)(calibrationValue - emptyValue)));
        double weight = ((readVal - emptyValue) * scaler);

        return (long) weight;
    }

    public void calibrateEmpty() throws InterruptedException {
        long calibrationValue = this.calibrationValue;
        long calibrationWeight = this.calibrationWeight;
        this.calibrationValue = 0;
        this.calibrationWeight = 0;
        this.emptyValue = 0;

        this.emptyValue = read();

        this.calibrationValue = calibrationValue;
        this.calibrationWeight = calibrationWeight;

    }

    public void calibrateEmpty(long emptyValue) {
        this.emptyValue = emptyValue;
    }

    public void calibrateWeighted(long calibrationWeight) throws InterruptedException {
        long emptyValue = this.emptyValue;
        this.calibrationValue = 0;
        this.calibrationWeight = 0;
        this.emptyValue = 0;

        this.calibrationValue = read();
        this.calibrationWeight = calibrationWeight;

        this.emptyValue = emptyValue;

    }

    public void calibrateWeighted(long calibrationWeight, long calibrationValue) {
        this.calibrationValue = calibrationValue;
        this.calibrationWeight = calibrationWeight;
    }

    public void setGain(int gain) throws InterruptedException {
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

        pinCLK.digitalWrite(PinState.LOW);
        read();
    }

    public boolean isReady() {
        return !pinDAT.isHigh();
    }

}
