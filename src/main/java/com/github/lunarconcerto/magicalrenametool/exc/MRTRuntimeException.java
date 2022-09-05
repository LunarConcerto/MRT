package com.github.lunarconcerto.magicalrenametool.exc;

public class MRTRuntimeException extends MRTException {

    public MRTRuntimeException() {
    }

    public MRTRuntimeException(String message) {
        super(message);
    }

    public MRTRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MRTRuntimeException(Throwable cause) {
        super(cause);
    }

    public MRTRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
