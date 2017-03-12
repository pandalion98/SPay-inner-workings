package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback.Stub;

public abstract class UpdateLoyaltyCardCallback implements PaymentFrameworkConnection {
    private PFUpdateLoyaltyCardCallback pfUlcCb;
    private UpdateLoyaltyCardCallback ulcCb;

    private class PFUpdateLoyaltyCardCallback extends Stub {
        private PFUpdateLoyaltyCardCallback() {
        }

        public void onSuccess(String str) {
            UpdateLoyaltyCardCallback.this.ulcCb.onSuccess(str);
        }

        public void onFail(int i) {
            UpdateLoyaltyCardCallback.this.ulcCb.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(String str);

    public UpdateLoyaltyCardCallback() {
        this.ulcCb = this;
        this.pfUlcCb = new PFUpdateLoyaltyCardCallback();
    }

    IUpdateLoyaltyCardCallback getPFUpdateLoyaltyCallback() {
        return this.pfUlcCb;
    }
}
