package com.samsung.android.spayfw.cncc;

import com.samsung.android.spaytzsvc.api.TAException;

public class CNCCTAException extends TAException {
    public static final int CNCC_INVALID_INPUT_BUFFER = 983042;
    public static final int CNCC_INVALID_INPUT_PARAM = 1;
    public static final int CNCC_OK = 0;
    public static final int CNCC_TA_COMMAND_FAILED = 4;
    public static final int CNCC_TA_NOT_LOADED = 3;
    public static final int CNCC_UNKNOWN_CMD = 983041;
    public static final int CNCC_UNKNOWN_FAILURE = 255;
    public static final int ERR_TZ_COM_ERR = 983040;

    public CNCCTAException(String str, int i) {
        super(str, i);
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
