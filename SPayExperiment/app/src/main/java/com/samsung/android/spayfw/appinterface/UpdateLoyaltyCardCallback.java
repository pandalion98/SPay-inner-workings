/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class UpdateLoyaltyCardCallback
implements PaymentFrameworkConnection {
    private PFUpdateLoyaltyCardCallback pfUlcCb = new PFUpdateLoyaltyCardCallback();
    private UpdateLoyaltyCardCallback ulcCb = this;

    IUpdateLoyaltyCardCallback getPFUpdateLoyaltyCallback() {
        return this.pfUlcCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(String var1);

    private class PFUpdateLoyaltyCardCallback
    extends IUpdateLoyaltyCardCallback.Stub {
        private PFUpdateLoyaltyCardCallback() {
        }

        @Override
        public void onFail(int n2) {
            UpdateLoyaltyCardCallback.this.ulcCb.onFail(n2);
        }

        @Override
        public void onSuccess(String string) {
            UpdateLoyaltyCardCallback.this.ulcCb.onSuccess(string);
        }
    }

}

