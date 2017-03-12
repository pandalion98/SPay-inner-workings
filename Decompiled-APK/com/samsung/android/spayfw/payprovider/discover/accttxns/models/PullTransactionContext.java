package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class PullTransactionContext {
    private SecureContext secureContext;
    private String txnAvailable;

    public PullTransactionContext(String str, SecureContext secureContext) {
        this.txnAvailable = str;
        this.secureContext = secureContext;
    }

    public SecureContext getSecureTransactionsList() {
        return this.secureContext;
    }

    public String getTransactionsAvailable() {
        return this.txnAvailable;
    }

    public static PullTransactionContext getInstance() {
        return new PullTransactionContext("true", new SecureContext());
    }
}
