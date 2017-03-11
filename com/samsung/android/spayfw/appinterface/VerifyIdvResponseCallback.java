package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback.Stub;

public abstract class VerifyIdvResponseCallback implements PaymentFrameworkConnection {
    private PFVerifyIdvCallback pfVerifyIdvCb;
    private VerifyIdvResponseCallback vidvcb;

    private class PFVerifyIdvCallback extends Stub {
        private PFVerifyIdvCallback() {
        }

        public void onSuccess(String str, Token token) {
            PaymentFramework.removeFromTrackMap(VerifyIdvResponseCallback.this.vidvcb);
            VerifyIdvResponseCallback.this.vidvcb.onSuccess(str, token);
        }

        public void onFail(String str, int i, Token token) {
            PaymentFramework.removeFromTrackMap(VerifyIdvResponseCallback.this.vidvcb);
            VerifyIdvResponseCallback.this.vidvcb.onFail(str, i, token);
        }
    }

    public abstract void onFail(String str, int i, Token token);

    public abstract void onSuccess(String str, Token token);

    public VerifyIdvResponseCallback() {
        this.vidvcb = this;
        this.pfVerifyIdvCb = new PFVerifyIdvCallback();
    }

    IVerifyIdvCallback getPFVerifyIdvCb() {
        return this.pfVerifyIdvCb;
    }
}
