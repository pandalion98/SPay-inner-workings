/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCCVMResult {
    public static final int CVM_RESULT_NOK = -1;
    public static final int CVM_RESULT_OK;
    private boolean mCVMRequired;
    private long mCVMResult;

    public MCCVMResult(long l2, boolean bl) {
        this.mCVMResult = l2;
        this.mCVMRequired = bl;
    }

    public long getResultCode() {
        return this.mCVMResult;
    }

    public boolean isCVMRequired() {
        return this.mCVMRequired;
    }
}

