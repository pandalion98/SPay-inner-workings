/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;

public abstract class ProvisionTokenCallback
implements PaymentFrameworkConnection {
    private PFProvisionTokenCallback pfProvisionCb = new PFProvisionTokenCallback();
    private ProvisionTokenCallback ptcb = this;

    IProvisionTokenCallback getPFProvisionCb() {
        return this.pfProvisionCb;
    }

    public abstract void onFail(String var1, int var2, ProvisionTokenResult var3);

    public abstract void onSuccess(String var1, ProvisionTokenResult var2);

    private class PFProvisionTokenCallback
    extends IProvisionTokenCallback.Stub {
        private PFProvisionTokenCallback() {
        }

        @Override
        public void onFail(String string, int n2, ProvisionTokenResult provisionTokenResult) {
            PaymentFramework.removeFromTrackMap(ProvisionTokenCallback.this.ptcb);
            ProvisionTokenCallback.this.ptcb.onFail(string, n2, provisionTokenResult);
        }

        @Override
        public void onSuccess(String string, ProvisionTokenResult provisionTokenResult) {
            PaymentFramework.removeFromTrackMap(ProvisionTokenCallback.this.ptcb);
            ProvisionTokenCallback.this.ptcb.onSuccess(string, provisionTokenResult);
        }
    }

}

