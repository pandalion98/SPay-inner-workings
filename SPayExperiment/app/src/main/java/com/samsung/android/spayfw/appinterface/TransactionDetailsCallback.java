/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.TransactionDetails;

public abstract class TransactionDetailsCallback
implements PaymentFrameworkConnection {
    private PFTransactionDetailsCallback pfTransactionDetailsCb = new PFTransactionDetailsCallback();
    private TransactionDetailsCallback pmcb = this;

    ITransactionDetailsCallback getPFTransactionDetailsCb() {
        return this.pfTransactionDetailsCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onTransactionUpdate(String var1, TransactionDetails var2);

    private class PFTransactionDetailsCallback
    extends ITransactionDetailsCallback.Stub {
        private PFTransactionDetailsCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(TransactionDetailsCallback.this.pmcb);
            TransactionDetailsCallback.this.pmcb.onFail(string, n2);
        }

        @Override
        public void onTransactionUpdate(String string, TransactionDetails transactionDetails) {
            PaymentFramework.removeFromTrackMap(TransactionDetailsCallback.this.pmcb);
            TransactionDetailsCallback.this.pmcb.onTransactionUpdate(string, transactionDetails);
        }
    }

}

