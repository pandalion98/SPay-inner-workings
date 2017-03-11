package com.samsung.android.spayfw.payprovider;

public class PaymentProviderException extends Exception {
    private int errorCode;

    public PaymentProviderException(int i) {
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
