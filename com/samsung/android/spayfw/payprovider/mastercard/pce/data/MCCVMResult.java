package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCCVMResult {
    public static final int CVM_RESULT_NOK = -1;
    public static final int CVM_RESULT_OK = 0;
    private boolean mCVMRequired;
    private long mCVMResult;

    public MCCVMResult(long j, boolean z) {
        this.mCVMResult = j;
        this.mCVMRequired = z;
    }

    public long getResultCode() {
        return this.mCVMResult;
    }

    public boolean isCVMRequired() {
        return this.mCVMRequired;
    }
}
