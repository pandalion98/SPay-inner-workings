package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback.Stub;

public abstract class GiftCardExtractDetailCallback implements PaymentFrameworkConnection {
    private GiftCardExtractDetailCallback cb;
    private PFGiftCardExtractDetailCallback pfCb;

    private class PFGiftCardExtractDetailCallback extends Stub {
        private PFGiftCardExtractDetailCallback() {
        }

        public void onSuccess(GiftCardDetail giftCardDetail) {
            PaymentFramework.removeFromTrackMap(GiftCardExtractDetailCallback.this.cb);
            GiftCardExtractDetailCallback.this.cb.onSuccess(giftCardDetail);
        }

        public void onFail(int i) {
            PaymentFramework.removeFromTrackMap(GiftCardExtractDetailCallback.this.cb);
            GiftCardExtractDetailCallback.this.cb.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(GiftCardDetail giftCardDetail);

    public GiftCardExtractDetailCallback() {
        this.cb = this;
        this.pfCb = new PFGiftCardExtractDetailCallback();
    }

    IGiftCardExtractDetailCallback getPFGiftCardExtractDetailCb() {
        return this.pfCb;
    }
}
