/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class UserSignatureCallback
implements PaymentFrameworkConnection {
    private UserSignatureCallback cb = this;
    private PFUserSignatureCallback pfCb = new PFUserSignatureCallback();

    IUserSignatureCallback getICallback() {
        return this.pfCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(byte[] var1);

    private class PFUserSignatureCallback
    extends IUserSignatureCallback.Stub {
        private PFUserSignatureCallback() {
        }

        @Override
        public void onFail(int n2) {
            PaymentFramework.removeFromTrackMap(UserSignatureCallback.this.cb);
            UserSignatureCallback.this.cb.onFail(n2);
        }

        @Override
        public void onSuccess(byte[] arrby) {
            PaymentFramework.removeFromTrackMap(UserSignatureCallback.this.cb);
            UserSignatureCallback.this.cb.onSuccess(arrby);
        }
    }

}

