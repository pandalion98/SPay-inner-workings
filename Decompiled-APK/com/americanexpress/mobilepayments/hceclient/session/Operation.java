package com.americanexpress.mobilepayments.hceclient.session;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;

public enum Operation {
    OPERATION;
    
    private OperationMode mode;
    private long realTimestamp;
    private long timestamp;
    private String tokenRefId;

    public OperationMode getMode() {
        return this.mode;
    }

    public void setMode(OperationMode operationMode) {
        this.mode = operationMode;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public void setTokenRefId(String str) {
        this.tokenRefId = str;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public long getRealTimestamp() {
        return this.realTimestamp;
    }

    public void setRealTimestamp(long j) {
        this.realTimestamp = j;
    }

    public Operation getOperation(OperationMode operationMode) {
        if (this.mode == null) {
            this.timestamp = System.currentTimeMillis();
            this.mode = operationMode;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.timestamp > this.mode.getTimeAllowed()) {
                this.timestamp = currentTimeMillis;
                this.mode = operationMode;
                SessionManager.getSession().clean();
            } else {
                throw new HCEClientException(operationMode.name() + " is in progress!!");
            }
        }
        return this;
    }

    public boolean releaseOperation() {
        if (this.mode == null) {
            return false;
        }
        this.mode = null;
        this.tokenRefId = null;
        this.timestamp = 0;
        this.realTimestamp = 0;
        return true;
    }
}
