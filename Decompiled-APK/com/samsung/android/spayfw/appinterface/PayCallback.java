package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IPayCallback.Stub;

public abstract class PayCallback implements PaymentFrameworkConnection {
    private PayCallback pcb;
    private PFPayCallback pfPayCb;

    private class PFPayCallback extends Stub {
        private PFPayCallback() {
        }

        public void onFinish(String str, int i, ApduReasonCode apduReasonCode) {
            PaymentFramework.removeFromTrackMap(PayCallback.this.pcb);
            PayCallback.this.pcb.onFinish(str, i, apduReasonCode);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(PayCallback.this.pcb);
            PayCallback.this.pcb.onFail(str, i);
        }

        public void onPay(String str, int i, int i2) {
            PayCallback.this.pcb.onPay(str, i, i2);
        }

        public void onPaySwitch(String str, int i, int i2) {
            PayCallback.this.pcb.onPaySwitch(str, i, i2);
        }

        public void onRetry(String str, int i, int i2) {
            PayCallback.this.pcb.onRetry(str, i, i2);
        }

        public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
            PayCallback.this.pcb.onExtractGiftCardDetail(giftCardDetail);
        }
    }

    public abstract void onExtractGiftCardDetail(GiftCardDetail giftCardDetail);

    public abstract void onFail(String str, int i);

    public abstract void onFinish(String str, int i, ApduReasonCode apduReasonCode);

    public abstract void onPay(String str, int i, int i2);

    public abstract void onPaySwitch(String str, int i, int i2);

    public abstract void onRetry(String str, int i, int i2);

    public PayCallback() {
        this.pcb = this;
        this.pfPayCb = new PFPayCallback();
    }

    IPayCallback getPFPayCb() {
        return this.pfPayCb;
    }
}
