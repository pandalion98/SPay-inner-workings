/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.session;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;

public final class Operation
extends Enum<Operation> {
    private static final /* synthetic */ Operation[] $VALUES;
    public static final /* enum */ Operation OPERATION = new Operation();
    private OperationMode mode;
    private long realTimestamp;
    private long timestamp;
    private String tokenRefId;

    static {
        Operation[] arroperation = new Operation[]{OPERATION};
        $VALUES = arroperation;
    }

    public static Operation valueOf(String string) {
        return (Operation)Enum.valueOf(Operation.class, (String)string);
    }

    public static Operation[] values() {
        return (Operation[])$VALUES.clone();
    }

    public OperationMode getMode() {
        return this.mode;
    }

    public Operation getOperation(OperationMode operationMode) {
        if (this.mode == null) {
            this.timestamp = System.currentTimeMillis();
            this.mode = operationMode;
            return this;
        }
        long l2 = System.currentTimeMillis();
        if (l2 - this.timestamp > this.mode.getTimeAllowed()) {
            this.timestamp = l2;
            this.mode = operationMode;
            SessionManager.getSession().clean();
            return this;
        }
        throw new HCEClientException(operationMode.name() + " is in progress!!");
    }

    public long getRealTimestamp() {
        return this.realTimestamp;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public boolean releaseOperation() {
        OperationMode operationMode = this.mode;
        boolean bl = false;
        if (operationMode != null) {
            this.mode = null;
            this.tokenRefId = null;
            this.timestamp = 0L;
            this.realTimestamp = 0L;
            bl = true;
        }
        return bl;
    }

    public void setMode(OperationMode operationMode) {
        this.mode = operationMode;
    }

    public void setRealTimestamp(long l2) {
        this.realTimestamp = l2;
    }

    public void setTimestamp(long l2) {
        this.timestamp = l2;
    }

    public void setTokenRefId(String string) {
        this.tokenRefId = string;
    }
}

