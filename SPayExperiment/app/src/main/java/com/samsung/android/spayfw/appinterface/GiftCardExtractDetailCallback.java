/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class GiftCardExtractDetailCallback
implements PaymentFrameworkConnection {
    private GiftCardExtractDetailCallback cb = this;
    private PFGiftCardExtractDetailCallback pfCb = new PFGiftCardExtractDetailCallback();

    IGiftCardExtractDetailCallback getPFGiftCardExtractDetailCb() {
        return this.pfCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(GiftCardDetail var1);

    private class PFGiftCardExtractDetailCallback
    extends IGiftCardExtractDetailCallback.Stub {
        private PFGiftCardExtractDetailCallback() {
        }

        @Override
        public void onFail(int n2) {
            PaymentFramework.removeFromTrackMap(GiftCardExtractDetailCallback.this.cb);
            GiftCardExtractDetailCallback.this.cb.onFail(n2);
        }

        @Override
        public void onSuccess(GiftCardDetail giftCardDetail) {
            PaymentFramework.removeFromTrackMap(GiftCardExtractDetailCallback.this.cb);
            GiftCardExtractDetailCallback.this.cb.onSuccess(giftCardDetail);
        }
    }

}

