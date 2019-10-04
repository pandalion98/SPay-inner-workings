/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.accttxns.models.PullTransactionContext;

public class Element {
    PullTransactionContext pullTxnContext;

    public Element(PullTransactionContext pullTransactionContext) {
        this.pullTxnContext = pullTransactionContext;
    }

    public static Element getInstance() {
        return new Element(PullTransactionContext.getInstance());
    }

    public PullTransactionContext getPullTxnContext() {
        return this.pullTxnContext;
    }
}

