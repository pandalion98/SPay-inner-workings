package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

public class AcctTransactionRecord {
    String tokenId;
    private AcctTransactionDetail txnDetail;
    String txnIdentifier;
    private AcctTransactionOperations txnOperations;

    public String getTokenId() {
        return this.tokenId;
    }

    public String getTransactionIdentifier() {
        return this.txnIdentifier;
    }

    public AcctTransactionDetail getTransactionDetail() {
        return this.txnDetail;
    }

    public AcctTransactionOperations getTransactionOperations() {
        return this.txnOperations;
    }
}
