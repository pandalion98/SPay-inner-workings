/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.TnC;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;

public abstract class PushMessageCallback
implements PaymentFrameworkConnection {
    private PFPushMsgCallback pfPushMsgCb = new PFPushMsgCallback();
    private PushMessageCallback pmcb = this;

    IPushMessageCallback getPFPushMsgCb() {
        return this.pfPushMsgCb;
    }

    public abstract void onCreateToken(String var1, String var2, Token var3);

    public abstract void onFail(String var1, int var2);

    public abstract void onTncUpdate(String var1, String var2, TnC var3);

    public abstract void onTokenMetaDataUpdate(String var1, String var2, TokenMetaData var3);

    public abstract void onTokenReplenishRequested(String var1, String var2);

    public abstract void onTokenReplenished(String var1, String var2);

    public abstract void onTokenStatusUpdate(String var1, String var2, TokenStatus var3);

    public abstract void onTransactionUpdate(String var1, String var2, TransactionDetails var3, boolean var4);

    private class PFPushMsgCallback
    extends IPushMessageCallback.Stub {
        private PFPushMsgCallback() {
        }

        @Override
        public void onCreateToken(String string, String string2, Token token) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onCreateToken(string, string2, token);
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onFail(string, n2);
        }

        @Override
        public void onTncUpdate(String string, String string2, TnC tnC) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTncUpdate(string, string2, tnC);
        }

        @Override
        public void onTokenMetaDataUpdate(String string, String string2, TokenMetaData tokenMetaData) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenMetaDataUpdate(string, string2, tokenMetaData);
        }

        @Override
        public void onTokenReplenishRequested(String string, String string2) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenReplenishRequested(string, string2);
        }

        @Override
        public void onTokenReplenished(String string, String string2) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenReplenished(string, string2);
        }

        @Override
        public void onTokenStatusUpdate(String string, String string2, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTokenStatusUpdate(string, string2, tokenStatus);
        }

        @Override
        public void onTransactionUpdate(String string, String string2, TransactionDetails transactionDetails, boolean bl) {
            PaymentFramework.removeFromTrackMap(PushMessageCallback.this.pmcb);
            PushMessageCallback.this.pmcb.onTransactionUpdate(string, string2, transactionDetails, bl);
        }
    }

}

