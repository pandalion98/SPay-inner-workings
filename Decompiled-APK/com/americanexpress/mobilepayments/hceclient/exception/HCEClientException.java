package com.americanexpress.mobilepayments.hceclient.exception;

import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;

public class HCEClientException extends Exception {
    private static final long serialVersionUID = -5390160483712125560L;
    private String errorCode;
    private short isoSW;
    private OperationStatus operationStatus;

    public HCEClientException(OperationStatus operationStatus) {
        super(operationStatus.getDetailMessage());
        this.operationStatus = operationStatus;
    }

    public HCEClientException(short s) {
        this.isoSW = s;
    }

    public HCEClientException(String str) {
        super(str);
    }

    public HCEClientException(String str, String str2) {
        super(str);
        this.errorCode = str2;
    }

    public HCEClientException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public HCEClientException(String str, String str2, Throwable th) {
        super(str);
        this.errorCode = str2;
        initCause(th);
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public short getIsoSW() {
        return this.isoSW;
    }

    public void setCause(Throwable th) {
        initCause(th);
    }

    public OperationStatus getOperationStatus() {
        return this.operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }
}
