package com.americanexpress.mobilepayments.hceclient.session;

public enum OperationMode {
    PROVISION(15000),
    PAYMENT(300),
    REFRESH(15000),
    TAP_PAYMENT(300),
    LCM(300);
    
    private long timeAllowed;

    public long getTimeAllowed() {
        return this.timeAllowed;
    }

    public void setTimeAllowed(long j) {
        this.timeAllowed = j;
    }

    private OperationMode(long j) {
        this.timeAllowed = j;
    }
}
