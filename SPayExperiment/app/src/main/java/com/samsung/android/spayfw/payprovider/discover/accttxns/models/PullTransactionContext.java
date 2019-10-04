/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class PullTransactionContext {
    private SecureContext secureContext;
    private String txnAvailable;

    public PullTransactionContext(String string, SecureContext secureContext) {
        this.txnAvailable = string;
        this.secureContext = secureContext;
    }

    public static PullTransactionContext getInstance() {
        return new PullTransactionContext("true", new SecureContext());
    }

    public SecureContext getSecureTransactionsList() {
        return this.secureContext;
    }

    public String getTransactionsAvailable() {
        return this.txnAvailable;
    }
}

