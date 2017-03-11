package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IInAppPayCallback.Stub;

public abstract class InAppPayCallback implements PaymentFrameworkConnection {
    private InAppPayCallback iapcb;
    private PFInAppPayCallback pfInAppPayCb;

    private class PFInAppPayCallback extends Stub {
        private PFInAppPayCallback() {
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(InAppPayCallback.this.iapcb);
            InAppPayCallback.this.iapcb.onFail(str, i);
        }

        public void onSuccess(String str, byte[] bArr) {
            PaymentFramework.removeFromTrackMap(InAppPayCallback.this.iapcb);
            InAppPayCallback.this.iapcb.onSuccess(str, bArr);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, byte[] bArr);

    public InAppPayCallback() {
        this.iapcb = this;
        this.pfInAppPayCb = new PFInAppPayCallback();
    }

    IInAppPayCallback getPFInAppPayCb() {
        return this.pfInAppPayCb;
    }
}
