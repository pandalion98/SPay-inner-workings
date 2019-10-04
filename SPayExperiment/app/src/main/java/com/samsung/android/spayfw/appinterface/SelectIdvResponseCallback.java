/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.SelectIdvResponse;

public abstract class SelectIdvResponseCallback
implements PaymentFrameworkConnection {
    private PFSelectIdvCallback pfSelectIdvCb = new PFSelectIdvCallback();
    private SelectIdvResponseCallback sidvcb = this;

    ISelectIdvCallback getPFSelectIdvCb() {
        return this.pfSelectIdvCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, SelectIdvResponse var2);

    private class PFSelectIdvCallback
    extends ISelectIdvCallback.Stub {
        private PFSelectIdvCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(SelectIdvResponseCallback.this.sidvcb);
            SelectIdvResponseCallback.this.sidvcb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, SelectIdvResponse selectIdvResponse) {
            PaymentFramework.removeFromTrackMap(SelectIdvResponseCallback.this.sidvcb);
            SelectIdvResponseCallback.this.sidvcb.onSuccess(string, selectIdvResponse);
        }
    }

}

