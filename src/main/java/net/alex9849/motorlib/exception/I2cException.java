package net.alex9849.motorlib.exception;

import com.pi4j.io.exception.IOException;

public class I2cException extends IOException {


    public I2cException(String message) {
        super(message);
    }

    public I2cException(Throwable cause) {
        super(cause);
    }

    public I2cException(String message, Throwable cause) {
        super(message, cause);
    }
}
