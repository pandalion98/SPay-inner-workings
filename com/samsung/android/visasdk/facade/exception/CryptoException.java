package com.samsung.android.visasdk.facade.exception;

public class CryptoException extends RuntimeException {
    private int errorCode;

    public CryptoException(String str) {
        super(str);
    }

    public CryptoException(int i, String str) {
        super(str);
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }
}
