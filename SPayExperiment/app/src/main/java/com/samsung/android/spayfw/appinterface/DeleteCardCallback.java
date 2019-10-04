/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.TokenStatus;

public abstract class DeleteCardCallback
implements PaymentFrameworkConnection {
    private PFDeleteCardCallback pfDeleteCardCb = new PFDeleteCardCallback();
    private DeleteCardCallback scb = this;

    IDeleteCardCallback getPFDeleteCardCb() {
        return this.pfDeleteCardCb;
    }

    public abstract void onFail(String var1, int var2, TokenStatus var3);

    public abstract void onSuccess(String var1, TokenStatus var2);

    private class PFDeleteCardCallback
    extends IDeleteCardCallback.Stub {
        private PFDeleteCardCallback() {
        }

        @Override
        public void onFail(String string, int n2, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(DeleteCardCallback.this.scb);
            DeleteCardCallback.this.scb.onFail(string, n2, tokenStatus);
        }

        @Override
        public void onSuccess(String string, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(DeleteCardCallback.this.scb);
            DeleteCardCallback.this.scb.onSuccess(string, tokenStatus);
        }
    }

}

