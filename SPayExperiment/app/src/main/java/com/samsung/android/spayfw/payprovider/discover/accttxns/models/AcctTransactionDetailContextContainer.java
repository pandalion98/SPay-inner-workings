/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionRecord;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTrasactionDetailContext;

public class AcctTransactionDetailContextContainer {
    private AcctTrasactionDetailContext txnDetailContext;

    public AcctTransactionRecord[] getTransactionsList() {
        return this.txnDetailContext.getTransactionsList();
    }
}

