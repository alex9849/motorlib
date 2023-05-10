package net.alex9849.pca9685port;

public class PCAChannels {
    private final PCA9685 pca;
    private final PWMChannel[] channels;

    public PCAChannels(PCA9685 pca) {
        this.pca = pca;
        this.channels = new PWMChannel[size()];
    }

    public int size() {
        return 16;
    }

    public PWMChannel get(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if(channels[index] == null) {
            channels[index] = new PWMChannel(pca, index);
        }
        return channels[index];
    }

}
