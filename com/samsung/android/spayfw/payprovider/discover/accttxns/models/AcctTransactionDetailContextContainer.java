package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

public class AcctTransactionDetailContextContainer {
    private AcctTrasactionDetailContext txnDetailContext;

    public AcctTransactionRecord[] getTransactionsList() {
        return this.txnDetailContext.getTransactionsList();
    }
}
