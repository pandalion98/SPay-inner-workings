package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

public class PlccTAException extends Exception {
    public static final int ERR_INVALID_INPUT = 1004;
    public static final int ERR_TZ_ACCESS_DENIED = 1002;
    public static final int ERR_TZ_COM_ERR = 1001;
    public static final int ERR_TZ_TA_NOT_AVAILABLE = 1003;
    public static final int ERR_UNKNOWN = 9001;
    private static final long serialVersionUID = 1;
    private int errorCode;

    public PlccTAException(String str, int i) {
        super(str);
        this.errorCode = ERR_UNKNOWN;
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
