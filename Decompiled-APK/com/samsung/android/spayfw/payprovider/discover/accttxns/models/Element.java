package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

public class Element {
    PullTransactionContext pullTxnContext;

    public PullTransactionContext getPullTxnContext() {
        return this.pullTxnContext;
    }

    public Element(PullTransactionContext pullTransactionContext) {
        this.pullTxnContext = pullTransactionContext;
    }

    public static Element getInstance() {
        return new Element(PullTransactionContext.getInstance());
    }
}
