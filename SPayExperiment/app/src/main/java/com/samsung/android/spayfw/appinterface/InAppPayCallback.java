/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class InAppPayCallback
implements PaymentFrameworkConnection {
    private InAppPayCallback iapcb = this;
    private PFInAppPayCallback pfInAppPayCb = new PFInAppPayCallback();

    IInAppPayCallback getPFInAppPayCb() {
        return this.pfInAppPayCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, byte[] var2);

    private class PFInAppPayCallback
    extends IInAppPayCallback.Stub {
        private PFInAppPayCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(InAppPayCallback.this.iapcb);
            InAppPayCallback.this.iapcb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, byte[] arrby) {
            PaymentFramework.removeFromTrackMap(InAppPayCallback.this.iapcb);
            InAppPayCallback.this.iapcb.onSuccess(string, arrby);
        }
    }

}

