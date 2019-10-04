/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCMSTOutputData {
    private static final long MST_CODE_BASE = 0L;
    public static final long MST_RESULT_CODE_INTERNAL_ERROR = 1L;
    public static final long MST_RESULT_CODE_PROFILE_NOT_FOUND = 2L;
    public static final long MST_RESULT_CODE_SUCCESS;
    private long mResultCode;

    public long getResultCode() {
        return this.mResultCode;
    }

    public void setResultCode(long l2) {
        this.mResultCode = l2;
    }
}

