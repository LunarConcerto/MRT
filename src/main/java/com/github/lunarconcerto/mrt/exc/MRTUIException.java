package com.github.lunarconcerto.mrt.exc;

public class MRTUIException extends MRTException {

    public MRTUIException() {
        super();
    }

    public MRTUIException(String message) {
        super(message);
    }

    public MRTUIException(String message, Throwable cause) {
        super(message, cause);
    }

    public MRTUIException(Throwable cause) {
        super(cause);
    }

    public MRTUIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
