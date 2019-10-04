/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenCloseResponse
extends TokenOperationStatus {
    private String transactionID;

    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(String string) {
        this.transactionID = string;
    }
}

