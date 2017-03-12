package com.samsung.sensorframework.sda;

public class SDAException extends Exception {
    private static final long serialVersionUID = -6952859423645368705L;
    private int errorCode;
    private String message;

    public SDAException(int i, String str) {
        super(str);
        this.errorCode = i;
        this.message = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}
