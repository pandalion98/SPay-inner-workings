/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.americanexpress.mobilepayments.hceclient.exception;

import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;

public class HCEClientException
extends Exception {
    private static final long serialVersionUID = -5390160483712125560L;
    private String errorCode;
    private short isoSW;
    private OperationStatus operationStatus;

    public HCEClientException(OperationStatus operationStatus) {
        super(operationStatus.getDetailMessage());
        this.operationStatus = operationStatus;
    }

    public HCEClientException(String string) {
        super(string);
    }

    public HCEClientException(String string, String string2) {
        super(string);
        this.errorCode = string2;
    }

    public HCEClientException(String string, String string2, Throwable throwable) {
        super(string);
        this.errorCode = string2;
        this.initCause(throwable);
    }

    public HCEClientException(String string, Throwable throwable) {
        super(string);
        this.initCause(throwable);
    }

    public HCEClientException(short s2) {
        this.isoSW = s2;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public short getIsoSW() {
        return this.isoSW;
    }

    public OperationStatus getOperationStatus() {
        return this.operationStatus;
    }

    public void setCause(Throwable throwable) {
        this.initCause(throwable);
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }
}

