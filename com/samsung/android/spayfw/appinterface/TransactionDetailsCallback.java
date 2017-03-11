package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback.Stub;

public abstract class TransactionDetailsCallback implements PaymentFrameworkConnection {
    private PFTransactionDetailsCallback pfTransactionDetailsCb;
    private TransactionDetailsCallback pmcb;

    private class PFTransactionDetailsCallback extends Stub {
        private PFTransactionDetailsCallback() {
        }

        public void onTransactionUpdate(String str, TransactionDetails transactionDetails) {
            PaymentFramework.removeFromTrackMap(TransactionDetailsCallback.this.pmcb);
            TransactionDetailsCallback.this.pmcb.onTransactionUpdate(str, transactionDetails);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(TransactionDetailsCallback.this.pmcb);
            TransactionDetailsCallback.this.pmcb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onTransactionUpdate(String str, TransactionDetails transactionDetails);

    public TransactionDetailsCallback() {
        this.pmcb = this;
        this.pfTransactionDetailsCb = new PFTransactionDetailsCallback();
    }

    ITransactionDetailsCallback getPFTransactionDetailsCb() {
        return this.pfTransactionDetailsCb;
    }
}
