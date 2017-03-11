package com.americanexpress.mobilepayments.hceclient.model;

public class TokenCloseResponse extends TokenOperationStatus {
    private String transactionID;

    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(String str) {
        this.transactionID = str;
    }
}
