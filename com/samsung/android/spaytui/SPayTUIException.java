package com.samsung.android.spaytui;

public class SPayTUIException extends Exception {
    public static final int ERR_INVALID_INPUT = 1004;
    public static final int ERR_TZ_ACCESS_DENIED = 1002;
    public static final int ERR_TZ_COM_ERR = 1001;
    public static final int ERR_TZ_TA_NOT_AVAILABLE = 1003;
    public static final int ERR_UNKNOWN = 9001;
    private int errorCode;

    public SPayTUIException(String str, int i) {
        super(str);
        this.errorCode = ERR_UNKNOWN;
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
