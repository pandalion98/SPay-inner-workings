package com.samsung.media.fmradio;

public class FMPlayerException extends Exception {
    public static final int AIRPLANE_MODE = 5;
    public static final int BATTERY_LOW = 6;
    public static final int HEAD_SET_IS_NOT_PLUGGED = 4;
    public static final int PLAYER_IS_NOT_ON = 1;
    public static final int PLAYER_SCANNING = 3;
    public static final int RADIO_SERVICE_DOWN = 2;
    public static final int TV_OUT_PLUGGED = 7;
    private static final long serialVersionUID = 1;
    private int mCode;
    private Throwable mThrowable;
    private String msg;

    public FMPlayerException(int code, String msg, Throwable throwable) {
        this.mCode = code;
        this.msg = msg;
        this.mThrowable = throwable;
    }

    public int getCode() {
        return this.mCode;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }

    public String getMessage() {
        return this.msg;
    }
}
