package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IPushMessageCallback.Stub;

public abstract class PushMessageCallback implements PaymentFrameworkConnection {
    private PFPushMsgCallback pfPushMsgCb;
    private PushMessageCallback pmcb;

    private class PFPushMsgCallback extends Stub {
        private PFPushMsgCallback() {
        }

        public void onTncUpdate(String str, String str2, TnC tnC) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTncUpdate(str, str2, tnC);
        }

        public void onTokenStatusUpdate(String str, String str2, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenStatusUpdate(str, str2, tokenStatus);
        }

        public void onTokenMetaDataUpdate(String str, String str2, TokenMetaData tokenMetaData) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenMetaDataUpdate(str, str2, tokenMetaData);
        }

        public void onCreateToken(String str, String str2, Token token) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onCreateToken(str, str2, token);
        }

        public void onTokenReplenishRequested(String str, String str2) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenReplenishRequested(str, str2);
        }

        public void onTokenReplenished(String str, String str2) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenReplenished(str, str2);
        }

        public void onTransactionUpdate(String str, String str2, TransactionDetails transactionDetails, boolean z) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTransactionUpdate(str, str2, transactionDetails, z);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onFail(str, i);
        }
    }

    public abstract void onCreateToken(String str, String str2, Token token);

    public abstract void onFail(String str, int i);

    public abstract void onTncUpdate(String str, String str2, TnC tnC);

    public abstract void onTokenMetaDataUpdate(String str, String str2, TokenMetaData tokenMetaData);

    public abstract void onTokenReplenishRequested(String str, String str2);

    public abstract void onTokenReplenished(String str, String str2);

    public abstract void onTokenStatusUpdate(String str, String str2, TokenStatus tokenStatus);

    public abstract void onTransactionUpdate(String str, String str2, TransactionDetails transactionDetails, boolean z);

    public PushMessageCallback() {
        this.pmcb = this;
        this.pfPushMsgCb = new PFPushMsgCallback();
    }

    IPushMessageCallback getPFPushMsgCb() {
        return this.pfPushMsgCb;
    }
}
