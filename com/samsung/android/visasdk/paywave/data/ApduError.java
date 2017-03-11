package com.samsung.android.visasdk.paywave.data;

import com.samsung.android.visasdk.paywave.Constant.State;

public class ApduError {
    private int errorCode;
    private String errorMessage;
    private State reasonCode;

    public ApduError(int i) {
        this.errorCode = 0;
        this.errorMessage = "no error";
        this.reasonCode = State.READY;
        this.errorCode = i;
    }

    public ApduError(int i, String str, State state) {
        this.errorCode = 0;
        this.errorMessage = "no error";
        this.reasonCode = State.READY;
        this.errorCode = i;
        this.errorMessage = str;
        this.reasonCode = state;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public State getReasonCode() {
        return this.reasonCode;
    }
}
