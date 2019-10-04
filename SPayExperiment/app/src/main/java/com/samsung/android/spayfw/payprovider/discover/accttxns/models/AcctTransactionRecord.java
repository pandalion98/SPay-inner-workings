/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionDetail;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionOperations;

public class AcctTransactionRecord {
    String tokenId;
    private AcctTransactionDetail txnDetail;
    String txnIdentifier;
    private AcctTransactionOperations txnOperations;

    public String getTokenId() {
        return this.tokenId;
    }

    public AcctTransactionDetail getTransactionDetail() {
        return this.txnDetail;
    }

    public String getTransactionIdentifier() {
        return this.txnIdentifier;
    }

    public AcctTransactionOperations getTransactionOperations() {
        return this.txnOperations;
    }
}

