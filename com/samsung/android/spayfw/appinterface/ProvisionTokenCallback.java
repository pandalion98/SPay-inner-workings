package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback.Stub;

public abstract class ProvisionTokenCallback implements PaymentFrameworkConnection {
    private PFProvisionTokenCallback pfProvisionCb;
    private ProvisionTokenCallback ptcb;

    private class PFProvisionTokenCallback extends Stub {
        private PFProvisionTokenCallback() {
        }

        public void onSuccess(String str, ProvisionTokenResult provisionTokenResult) {
            PaymentFramework.removeFromTrackMap(ProvisionTokenCallback.this.ptcb);
            ProvisionTokenCallback.this.ptcb.onSuccess(str, provisionTokenResult);
        }

        public void onFail(String str, int i, ProvisionTokenResult provisionTokenResult) {
            PaymentFramework.removeFromTrackMap(ProvisionTokenCallback.this.ptcb);
            ProvisionTokenCallback.this.ptcb.onFail(str, i, provisionTokenResult);
        }
    }

    public abstract void onFail(String str, int i, ProvisionTokenResult provisionTokenResult);

    public abstract void onSuccess(String str, ProvisionTokenResult provisionTokenResult);

    public ProvisionTokenCallback() {
        this.ptcb = this;
        this.pfProvisionCb = new PFProvisionTokenCallback();
    }

    IProvisionTokenCallback getPFProvisionCb() {
        return this.pfProvisionCb;
    }
}
