/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPResultCode;

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

