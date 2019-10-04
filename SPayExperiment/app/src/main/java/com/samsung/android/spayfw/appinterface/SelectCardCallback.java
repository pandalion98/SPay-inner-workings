/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.SelectCardResult;

public abstract class SelectCardCallback
implements PaymentFrameworkConnection {
    private PFSelectCardCallback pfSelectCardCb = new PFSelectCardCallback();
    private SelectCardCallback scb = this;

    ISelectCardCallback getPFSelectCardCb() {
        return this.pfSelectCardCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, SelectCardResult var2);

    private class PFSelectCardCallback
    extends ISelectCardCallback.Stub {
        private PFSelectCardCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(SelectCardCallback.this.scb);
            SelectCardCallback.this.scb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, SelectCardResult selectCardResult) {
            PaymentFramework.removeFromTrackMap(SelectCardCallback.this.scb);
            SelectCardCallback.this.scb.onSuccess(string, selectCardResult);
        }
    }

}

