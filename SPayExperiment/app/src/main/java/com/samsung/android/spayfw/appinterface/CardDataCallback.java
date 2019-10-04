/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.Token;

public abstract class CardDataCallback
implements PaymentFrameworkConnection {
    private PFCardDataCallback pfCardDataCb = new PFCardDataCallback();
    private CardDataCallback scb = this;

    ICardDataCallback getPFCardDataCb() {
        return this.pfCardDataCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, Token var2);

    private class PFCardDataCallback
    extends ICardDataCallback.Stub {
        private PFCardDataCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(CardDataCallback.this.scb);
            CardDataCallback.this.scb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, Token token) {
            PaymentFramework.removeFromTrackMap(CardDataCallback.this.scb);
            CardDataCallback.this.scb.onSuccess(string, token);
        }
    }

}

