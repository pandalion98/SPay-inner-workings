/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.Token;

public abstract class VerifyIdvResponseCallback
implements PaymentFrameworkConnection {
    private PFVerifyIdvCallback pfVerifyIdvCb = new PFVerifyIdvCallback();
    private VerifyIdvResponseCallback vidvcb = this;

    IVerifyIdvCallback getPFVerifyIdvCb() {
        return this.pfVerifyIdvCb;
    }

    public abstract void onFail(String var1, int var2, Token var3);

    public abstract void onSuccess(String var1, Token var2);

    private class PFVerifyIdvCallback
    extends IVerifyIdvCallback.Stub {
        private PFVerifyIdvCallback() {
        }

        @Override
        public void onFail(String string, int n2, Token token) {
            PaymentFramework.removeFromTrackMap(VerifyIdvResponseCallback.this.vidvcb);
            VerifyIdvResponseCallback.this.vidvcb.onFail(string, n2, token);
        }

        @Override
        public void onSuccess(String string, Token token) {
            PaymentFramework.removeFromTrackMap(VerifyIdvResponseCallback.this.vidvcb);
            VerifyIdvResponseCallback.this.vidvcb.onSuccess(string, token);
        }
    }

}

