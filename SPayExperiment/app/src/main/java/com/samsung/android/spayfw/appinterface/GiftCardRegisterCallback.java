/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class GiftCardRegisterCallback
implements PaymentFrameworkConnection {
    private GiftCardRegisterCallback cb = this;
    private PFGiftCardRegisterCallback pfCb = new PFGiftCardRegisterCallback();

    IGiftCardRegisterCallback getPFGiftCardRegisterCb() {
        return this.pfCb;
    }

    public abstract void onFail(int var1, GiftCardRegisterResponseData var2);

    public abstract void onSuccess(GiftCardRegisterResponseData var1);

    private class PFGiftCardRegisterCallback
    extends IGiftCardRegisterCallback.Stub {
        private PFGiftCardRegisterCallback() {
        }

        @Override
        public void onFail(int n2, GiftCardRegisterResponseData giftCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GiftCardRegisterCallback.this.cb);
            GiftCardRegisterCallback.this.cb.onFail(n2, giftCardRegisterResponseData);
        }

        @Override
        public void onSuccess(GiftCardRegisterResponseData giftCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GiftCardRegisterCallback.this.cb);
            GiftCardRegisterCallback.this.cb.onSuccess(giftCardRegisterResponseData);
        }
    }

}

