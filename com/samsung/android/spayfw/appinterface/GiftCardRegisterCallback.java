package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback.Stub;

public abstract class GiftCardRegisterCallback implements PaymentFrameworkConnection {
    private GiftCardRegisterCallback cb;
    private PFGiftCardRegisterCallback pfCb;

    private class PFGiftCardRegisterCallback extends Stub {
        private PFGiftCardRegisterCallback() {
        }

        public void onSuccess(GiftCardRegisterResponseData giftCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GiftCardRegisterCallback.this.cb);
            GiftCardRegisterCallback.this.cb.onSuccess(giftCardRegisterResponseData);
        }

        public void onFail(int i, GiftCardRegisterResponseData giftCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GiftCardRegisterCallback.this.cb);
            GiftCardRegisterCallback.this.cb.onFail(i, giftCardRegisterResponseData);
        }
    }

    public abstract void onFail(int i, GiftCardRegisterResponseData giftCardRegisterResponseData);

    public abstract void onSuccess(GiftCardRegisterResponseData giftCardRegisterResponseData);

    public GiftCardRegisterCallback() {
        this.cb = this;
        this.pfCb = new PFGiftCardRegisterCallback();
    }

    IGiftCardRegisterCallback getPFGiftCardRegisterCb() {
        return this.pfCb;
    }
}
