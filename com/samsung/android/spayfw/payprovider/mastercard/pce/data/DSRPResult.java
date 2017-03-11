package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class DSRPResult {
    private final DSRPOutputData data;
    private final DSRPResultCode resultCode;

    public DSRPResult(DSRPResultCode dSRPResultCode, DSRPOutputData dSRPOutputData) {
        this.data = dSRPOutputData;
        this.resultCode = dSRPResultCode;
    }

    public DSRPOutputData getData() {
        return this.data;
    }

    public DSRPResultCode getResultCode() {
        return this.resultCode;
    }
}
