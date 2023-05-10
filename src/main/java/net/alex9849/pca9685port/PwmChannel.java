package net.alex9849.pca9685port;

public class PwmChannel {
    private PCA9685 pca;
    private int index;

    public PwmChannel(PCA9685 pca, int index) {
        this.pca = pca;
        this.index = index;
    }

    public float getFrequency() {
        throw new IllegalStateException("Not implemented!");
    }

    public int duty_cycle() {
        throw new IllegalStateException("Not implemented!");
    }

}
