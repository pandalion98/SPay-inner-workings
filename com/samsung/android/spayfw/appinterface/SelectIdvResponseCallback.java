package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ISelectIdvCallback.Stub;

public abstract class SelectIdvResponseCallback implements PaymentFrameworkConnection {
    private PFSelectIdvCallback pfSelectIdvCb;
    private SelectIdvResponseCallback sidvcb;

    private class PFSelectIdvCallback extends Stub {
        private PFSelectIdvCallback() {
        }

        public void onSuccess(String str, SelectIdvResponse selectIdvResponse) {
            PaymentFramework.removeFromTrackMap(SelectIdvResponseCallback.this.sidvcb);
            SelectIdvResponseCallback.this.sidvcb.onSuccess(str, selectIdvResponse);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(SelectIdvResponseCallback.this.sidvcb);
            SelectIdvResponseCallback.this.sidvcb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, SelectIdvResponse selectIdvResponse);

    public SelectIdvResponseCallback() {
        this.sidvcb = this;
        this.pfSelectIdvCb = new PFSelectIdvCallback();
    }

    ISelectIdvCallback getPFSelectIdvCb() {
        return this.pfSelectIdvCb;
    }
}
