package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCMSTOutputData {
    private static final long MST_CODE_BASE = 0;
    public static final long MST_RESULT_CODE_INTERNAL_ERROR = 1;
    public static final long MST_RESULT_CODE_PROFILE_NOT_FOUND = 2;
    public static final long MST_RESULT_CODE_SUCCESS = 0;
    private long mResultCode;

    public long getResultCode() {
        return this.mResultCode;
    }

    public void setResultCode(long j) {
        this.mResultCode = j;
    }
}
