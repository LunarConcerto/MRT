package com.github.lunarconcerto.magicalrenametool.exc;

public class MRTRuntimeException extends MRTException {

    public MRTRuntimeException() {
        printStackTrace();
    }

    public MRTRuntimeException(String message) {
        super(message);

        printStackTrace();
    }

    public MRTRuntimeException(String message, Throwable cause) {
        super(message, cause);

        printStackTrace();
    }

    public MRTRuntimeException(Throwable cause) {
        super(cause);

        printStackTrace();
    }

    public MRTRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        printStackTrace();
    }

}
