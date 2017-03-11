package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IUserSignatureCallback.Stub;

public abstract class UserSignatureCallback implements PaymentFrameworkConnection {
    private UserSignatureCallback cb;
    private PFUserSignatureCallback pfCb;

    private class PFUserSignatureCallback extends Stub {
        private PFUserSignatureCallback() {
        }

        public void onSuccess(byte[] bArr) {
            PaymentFramework.removeFromTrackMap(UserSignatureCallback.this.cb);
            UserSignatureCallback.this.cb.onSuccess(bArr);
        }

        public void onFail(int i) {
            PaymentFramework.removeFromTrackMap(UserSignatureCallback.this.cb);
            UserSignatureCallback.this.cb.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(byte[] bArr);

    public UserSignatureCallback() {
        this.cb = this;
        this.pfCb = new PFUserSignatureCallback();
    }

    IUserSignatureCallback getICallback() {
        return this.pfCb;
    }
}
