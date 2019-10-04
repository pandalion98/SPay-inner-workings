/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.data;

import com.samsung.android.visasdk.paywave.Constant;

public class ApduError {
    private int errorCode = 0;
    private String errorMessage = "no error";
    private Constant.State reasonCode = Constant.State.DP;

    public ApduError(int n2) {
        this.errorCode = n2;
    }

    public ApduError(int n2, String string, Constant.State state) {
        this.errorCode = n2;
        this.errorMessage = string;
        this.reasonCode = state;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Constant.State getReasonCode() {
        return this.reasonCode;
    }
}

