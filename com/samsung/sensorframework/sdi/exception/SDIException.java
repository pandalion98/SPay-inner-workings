package com.samsung.sensorframework.sdi.exception;

public class SDIException extends Exception {
    private int errorCode;
    private String message;

    public SDIException(int i, String str) {
        super(str);
        this.errorCode = i;
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }
}
