package com.visa.tainterface;

public class VisaTAException extends Exception {
    private int errorCode;

    public VisaTAException(String str, int i) {
        super(str);
        this.errorCode = i;
    }
}
