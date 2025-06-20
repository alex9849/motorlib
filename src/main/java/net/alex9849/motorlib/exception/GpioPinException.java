package net.alex9849.motorlib.exception;

import com.pi4j.io.exception.IOException;

public class GpioPinException extends IOException {


    public GpioPinException(String message) {
        super(message);
    }

    public GpioPinException(Throwable cause) {
        super(cause);
    }

    public GpioPinException(String message, Throwable cause) {
        super(message, cause);
    }
}
